package com.flatsharehunting.handleDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    /**
     * Connect to database.db
     * @return Connection conn : connection object to database.db
     */
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


    /**
     * Insert into (table) (columns) values (values)
     * @param String tableName : name of the table
     * @param String columns : list of columns
     * @param String values : list of values
     */
    public static void insert(String tableName, String columns, String values) {
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Select (columns) from (table)
     * @param String columns : list of columns
     * @param String tableName : name of the table
     * @return List<Map<String, Object>> rows : list of rows, each row is a map, key is column name, value is column value
     */
    public static List<Map<String, Object>> select(String columns, String tableName) {
        String sql = "SELECT " + columns + " FROM " + tableName;
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnName(i);
                    Object colVal = rs.getObject(i);
                    row.put(colName, colVal);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rows;
    }

    /**
     * Select (columns) from (table) where (condition)
     * @param String columns : list of columns
     * @param String tableName : name of the table
     * @param String condition : condition
     * @return List<Map<String, Object>> rows : list of rows, each row is a map, key is column name, value is column value
     */
    public static List<Map<String, Object>> select(String columns, String tableName, String condition) {
        String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + condition;
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnName(i);
                    Object colVal = rs.getObject(i);
                    row.put(colName, colVal);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rows;

    }

    // tests
    public static void main(String[] args) throws SQLException {

    }
}
