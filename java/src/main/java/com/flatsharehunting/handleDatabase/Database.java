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

    private static Boolean debug = false;

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
     * @param tableName String : name of the table
     * @param columns String : list of columns
     * @param values String : list of values
     */
    public static void insert(String tableName, String columns, String values) {
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        if(debug){System.out.println(sql);}

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Select (columns) from (table)
     * @param columns String : list of columns
     * @param tableName String : name of the table
     * @return List<Map<String, Object>> rows : list of rows, each row is a map, key is column name, value is column value
     */
    public static List<Map<String, Object>> select(String columns, String tableName) {
        String sql = "SELECT " + columns + " FROM " + tableName;
        List<Map<String, Object>> rows = new ArrayList<>();

        if(debug){System.out.println(sql);}

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
     * @param columns String : list of columns
     * @param tableName String : name of the table
     * @param condition String : condition
     * @return List<Map<String, Object>> rows : list of rows, each row is a map, key is column name, value is column value
     */
    public static List<Map<String, Object>> select(String columns, String tableName, String condition) {
        String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + condition;
        List<Map<String, Object>> rows = new ArrayList<>();

        if(debug){System.out.println(sql);}

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
     * Select (sqlRequest)
     * @param sqlRequest
     * @return List<Map<String, Object>> rows : list of rows, each row is a map, key is column name, value is column value
     */
    public static List<Map<String, Object>> select(String sqlRequest){
        List<Map<String, Object>> rows = new ArrayList<>();

        if(debug){System.out.println(sqlRequest);}

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlRequest);
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
     * Update (table) set (column) = (value) where (condition)
     * @param tableName String : name of the table
     * @param column String : column to update
     * @param value String : value to set
     * @param condition String : condition
     */
    public static void update(String tableName, String column, String value, String condition) {
        String sql = "UPDATE " + tableName + " SET " + column + " = " + value + " WHERE " + condition;

        if(debug){System.out.println(sql);}

        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // tests
    public static void main(String[] args) throws SQLException {
        update("Personne", "idProjetColoc", "1", "idPersonne='-1871158381'");
    }
}
