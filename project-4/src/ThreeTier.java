/*
 * Name: Luis Vargas
 * Course: CNT 4714 - Spring 2022
 * Assignment Title: Project 4
 * Due Date: Sunday, April 24, 2022
 */
package src;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.*;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreeTier{
    private boolean dbConnected = false;
    private Connection dsCon;
    private DatabaseMetaData dbMetaData;
    private MysqlDataSource ds;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData RSmetaData;
    private int colCount;
    private String[] colName;
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
    private int num_queries;
    private int num_updates;
    private Connection ops_log_conn;

    // Root or client
    private void initRootDB(){
        try {
            ds = new MysqlDataSource();
            ds.setURL("jdbc:mysql://localhost:3306/project4?useTimezone=true&serverTimezone=UTC");
            ds.setUser("root");
            ds.setPassword("root");
            dsCon = ds.getConnection();

        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }
    private void initClientDB(){
        try{
            ds = new MysqlDataSource();
            ds.setURL("jdbc:mysql://localhost:3306/project4?useTimezone=true&serverTimezone=UTC");
            ds.setUser("client");
            ds.setPassword("client");
            dsCon = ds.getConnection();
        }
    }
}
