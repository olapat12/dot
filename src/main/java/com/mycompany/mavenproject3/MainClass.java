/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3;

import com.mycompany.mavenproject3.database.BlockedIpTable;
import com.mycompany.mavenproject3.database.UserAccessLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author CROWN-STAFF
 */

//before your run the application, kindly replace the file path with your own file path in the UserAccessLog.java
// also edit the config.java and change the connection name, create a database req_limit and run this query 

//drop table `user_access_log`;

//CREATE TABLE `user_access_log` (
//  `id` int(11) NOT NULL AUTO_INCREMENT,
//  `date` datetime NOT NULL,
//  `ip` varchar(20) NOT NULL,
//  `request` varchar(50) NOT NULL,
//  `status` int NOT NULL,
//  `user_agent` varchar(200) NOT NULL,
//  PRIMARY KEY (`id`)
//);

//drop table if exists `blocked_ip_table`;
//
//CREATE TABLE `blocked_ip_table` (
//  `id` int(11) NOT NULL AUTO_INCREMENT,
//  `ip` varchar(20) NOT NULL,
//  `request_number` long NOT NULL,
//  `comment` varchar(300) NOT NULL,
//  PRIMARY KEY (`id`)
//)
public class MainClass {
    
    
      
      public static void main(String[] args) {
          
          // creating new hashset to avoid duplicate of data
          HashSet<String> set = new HashSet<>();
          
          
          List<UserAccessLog> data = new ArrayList<>();
          
          UserAccessLog userlog = new UserAccessLog();
//          BlockedIpTable blockedIp = new BlockedIpTable();
          
          try {
              // to write into database  
              userlog.readData();
              
              // method to get data between two dates
              data = userlog.getData();
              
              // method to process data and find the one that has made over 100 or 250 request in the last one hr
              userlog.ipToBeBlocked(data);
              
          }catch(Exception e){
              System.out.println(e);
          }


    }
   
    
}
