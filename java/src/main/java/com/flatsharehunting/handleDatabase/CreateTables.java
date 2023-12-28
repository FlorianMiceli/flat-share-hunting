package com.flatsharehunting.handleDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {

    // tests from @author sqlitetutorial.net
    public static void createNewTable() {
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS test (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL\n"
                + ");";
        
        try (Connection conn = SQL.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        createNewTable();
    }

}
