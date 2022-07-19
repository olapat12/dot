/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author CROWN-STAFF
 */
public class UserAccessLog {
    
    private int id;
    private String date;
    private String ip;
    private String request;
    private int status;
    private String user_agent;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }
    
   
    // method to read user_access.txt
    public void readData(){
        
             BufferedReader br = null;
             
             String query = "insert into user_access_log (date, ip, request,status,user_agent) values (?,?,?,?,?)";

          try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader("C:\\Users\\CROWN-STAFF\\Documents\\user_access.txt"));//file name with path and please kindly replace the file path
                while ((sCurrentLine = br.readLine()) != null) {
                String[] strArr = sCurrentLine.split("\\|");
                  for(String str:strArr){
                   str = str.replace("\"", "");
                   date = strArr[0];
                   ip = strArr[1];
                   request = strArr[2].replace("\"", "");
                   status = Integer.parseInt(strArr[3]);
                   user_agent = strArr[4].replace("\"", "");
                   }
                  saveData(query);
              }

          }catch (IOException e) {
             e.printStackTrace();
          }finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    // method to insert into user_access_log
     private void saveData(String query){
         Config connect;
         
        connect = new Config();
        
          try(Connection conn = connect.connect();
                PreparedStatement pstat = conn.prepareStatement(query)){
                pstat.setString(1, date);
                pstat.setString(2, ip);
                pstat.setString(3, request);
                pstat.setInt(4, status);
                pstat.setString(5, user_agent);
                
                pstat.executeUpdate();
                pstat.close();
          }catch(SQLException e){
                System.out.println(e);
          }
      }
     
     
     // method to retrieve records from user_access_log between two dates
     public List<UserAccessLog> getData(){
         
         ArrayList<UserAccessLog> recordData = new ArrayList<>();

         Config connect;
         
         connect = new Config();
         
          try(Connection conn = connect.connect();
              PreparedStatement st = conn.prepareStatement("select * from user_access_log where (date BETWEEN '2022-01-01 13:00:00' AND '2022-01-01 14:00:00')")){
                ResultSet rs = st.executeQuery();
                
                while(rs.next()){

                    UserAccessLog record = new UserAccessLog();
                    
                    int id = rs.getInt("id");
                    String date = rs.getString("date");
                    String ip = rs.getString("ip");
                    String request = rs.getString("request");
                    int status = rs.getInt("status");
                    String user_agent = rs.getString("user_agent");

                    record.setId(id);
                    record.setDate(date);
                    record.setIp(ip);
                    record.setRequest(request);
                    record.setStatus(status);
                    record.setUser_agent(user_agent);

                    recordData.add(record);
              }
                rs.close();
//                System.out.println(recordData.toString());
          }catch(SQLException e){
                System.out.println(e);
          }
        return recordData;
     }
     
     public void ipToBeBlocked(List<UserAccessLog> data){
         
          HashSet<String> set = new HashSet<>();
          
           BlockedIpTable blockedIp = new BlockedIpTable();
          
          try{
               long total;
              
                for(UserAccessLog userLog : data){
                    
                    // to get all ip within the range and number of request made with an hr
                    total = data.stream().filter(datas -> userLog.getIp().equals(datas.getIp())).count();
                    
                    // add the result into a hashset
                    set.add(userLog.getIp() + "="+total); 
                    
                    HashSet<String> hash = new HashSet<>();
                
                // iterating through the set to find an ip that has made more than 100 or 250 request within an hr
                Iterator<String> it = set.iterator();
                 while (it.hasNext()) {
                    String str = it.next();
                    // split the string and separate the ip and request made within an hr
                    String[] result = str.split("[=]");
                    String ip = result[0];
                    int occur = Integer.parseInt(result[1]);
                    // check for 100 request and above but below 250 in the last 1 hr
                    if(occur >100 && occur <=250){
                        // if the condition is true, save the ip address and and total request in the blocked_ip_table
                        blockedIp.saveData(ip, occur);
                        hash.add("Ip Address = " + ip + " request in the last an hr = " + occur + " ");
//                        System.out.println("Ip Address = " + ip + " request in the last an hr = " + occur + " " );
                        
                    }
                    // check for 250 request and above in the last 1 hr
                    else if(occur > 250){
                        // if the condition is true, save the ip address and and total request in the blocked_ip_table
                        blockedIp.saveData(ip, occur);
                        hash.add("Ip Address = " + ip + " request in the last an hr = " + occur + " ");
//                        System.out.println("Ip Address = " + ip + " request in the last an hr = " + occur + " " );
                    }
                  }
               
               System.out.println(hash.toString());
               } 
          }catch(Exception e){
              System.out.println(e);
          }
          
//          List<UserAccessLog> data = new ArrayList<>();
     }
}
    

