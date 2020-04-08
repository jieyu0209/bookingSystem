package booksys.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
  public static void main(String[] args) {
    String url = "jdbc:sqlite:./bs.db";

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE  IF NOT EXISTS Oid (\r\n" + 
            "       last_id      INT NOT NULL\r\n" + 
            ") ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "CREATE TABLE  IF NOT EXISTS `Table` (\r\n" + 
            "       number      INT NOT NULL,\r\n" + 
            "       places      INT NOT NULL\r\n" + 
            ") ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "CREATE TABLE  IF NOT EXISTS Customer (\r\n" + 
            "       name     VARCHAR(32) NOT NULL,\r\n" + 
            "       phoneNumber  CHAR(13) NOT NULL\r\n" + 
            ") ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "CREATE TABLE WalkIn (\r\n" + 
            "       oid      int NOT NULL PRIMARY KEY,\r\n" + 
            "       covers     int,\r\n" + 
            "       date     VARCHAR(32),\r\n" + 
            "       time     VARCHAR(32),\r\n" + 
            "       table_id     int\r\n" + 
            ") ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "CREATE TABLE  IF NOT EXISTS Reservation (\r\n" + 
            "       oid     int NOT NULL PRIMARY KEY,\r\n" + 
            "       covers     int,\r\n" + 
            "       date     VARCHAR(32),\r\n" + 
            "       time     VARCHAR(32),\r\n" + 
            "       table_id     int,\r\n" + 
            "       customer_id  int,\r\n" + 
            "       arrivalTime  VARCHAR(32)\r\n" + 
            ") ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO Oid VALUES (0) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (1, 2) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (2, 2) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (3, 2) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (4, 2) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (5, 4) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (6, 4) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (7, 4) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (8, 4) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (9, 4) ;";
        stmt.execute(sql);
        stmt = conn.createStatement();
        sql = "INSERT INTO `Table` (number, places) VALUES (10, 4) ;";
        stmt.execute(sql);
        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
