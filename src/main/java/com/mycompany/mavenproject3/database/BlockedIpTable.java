/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author CROWN-STAFF
 */
public class BlockedIpTable {
    
    private int id;
    private String ip;
    private int request_number;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRequest_number() {
        return request_number;
    }

    public void setRequest_number(int request_number) {
        this.request_number = request_number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public void saveData(String ip, long request_number){
        
        String query = "insert into blocked_ip_table (ip, request_number,comment) values (?,?,?)";
        
         Config connect;
         
        connect = new Config();
        
          try(Connection conn = connect.connect();
                PreparedStatement pstat = conn.prepareStatement(query)){
     
                pstat.setString(1, ip);
                pstat.setLong(2, request_number);
                pstat.setString(3, "You have been blocked because you have requested above " + request_number + " requests in the last 1 hour ");
                
                pstat.executeUpdate();
                pstat.close();
          }catch(SQLException e){
                System.out.println(e);
          }
    }
    
    public boolean findIp(String ip){
         String query = "select ip from blocked_ip_table where ip = ?";
         boolean available = false;
         Config connect;
         
         connect = new Config();
         
          try(Connection conn = connect.connect();
                PreparedStatement pstat = conn.prepareStatement(query)){
     
                pstat.setString(1, ip);
                
                ResultSet rs = pstat.executeQuery(); 
                
                if(rs.next()) {
                    available = true;
                } 
                else{
                    available = false;
                } 
                pstat.close();
          }catch(SQLException e){
                System.out.println(e);
          }
          return available;
    }
    
}
