package com.chatapp;

import java.sql.*;
import java.util.*;

public class dbconnection {
    
    Connection c;
    
    dbconnection() {
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "");
            c.createStatement().execute("CREATE TABLE IF NOT EXISTS chat_history (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), message TEXT)");
            System.out.println("DB connected");
        } catch (Exception e) {
            System.out.println("DB error: " + e);
        }
    }
    
    void save(String name, String msg) {
        try {
            PreparedStatement ps = c.prepareStatement("INSERT INTO chat_history (username, message) VALUES (?,?)");
            ps.setString(1, name);
            ps.setString(2, msg);
            ps.execute();
        } catch (Exception e) {}
    }
    
    List<String> get() {
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT username, message FROM chat_history ORDER BY id DESC LIMIT 10");
            while (rs.next()) {
                list.add(rs.getString("username") + ": " + rs.getString("message"));
            }
        } catch (Exception e) {}
        return list;
    }
}