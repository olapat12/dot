/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject3.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author CROWN-STAFF
 */
public class Config {
    
    
      public Connection connect(){
          try{
             Class.forName("com.mysql.jdbc.Driver");
             return DriverManager.getConnection("jdbc:mysql://localhost:3306/req_limit", "root", "adebolami");
          }catch(SQLException | ClassNotFoundException e){
              System.out.println(e);
              return null;
          }
      }
    
}
