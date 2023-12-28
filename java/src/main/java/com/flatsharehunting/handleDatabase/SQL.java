package com.flatsharehunting.handleDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SQL {

    // Connect to database.db
    public static Connection connect() {
        String url = "jdbc:sqlite:data/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;    
    }

    // Insert into (table) (columns) values (values)
    public void insert(String table, String columns, String values) {
        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";

        try (Connection conn = SQL.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // tests
    public static void main(String[] args) {

        SQL db = new SQL();
        // insert ('112', 'testname') into test
        db.insert("test", "id, name", "115, 'testname'");
    }
}
