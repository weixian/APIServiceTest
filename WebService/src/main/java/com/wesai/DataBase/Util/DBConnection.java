package com.wesai.DataBase.Util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DBConnection {

  /**
   * @param args
   */
  String driver = "com.mysql.jdbc.Driver"; // ����������

  String url = "jdbc:mysql://172.16.129.144:3306/ws_o2o"; // URLָ��Ҫ���ʵ����ݿ���scutcs

  String user = "ws_o2o"; // MySQL����ʱ���û���

  String password = "ws_o2o#123"; // MySQL����ʱ������

  private Connection conn = null;

  private Statement stmt = null;

  private PreparedStatement prepstmt = null;


  public DBConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, user, password);
      stmt = conn.createStatement();

      // System.out.println("Connected!");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public int ConnectDB(String DName, String DUser, String pw) {
    try {
      Class.forName("com.mysql.jdbc.Driver");

      conn = DriverManager.getConnection(DName, DUser, pw);

      stmt = conn.createStatement();

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (conn != null) {
      return 0;
    }
    return -1;
  }

  public DBConnection(String sql) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, user, password);
      this.prepareStatement(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public void prepareStatement(String sql) {
    try {
      prepstmt = conn.prepareStatement(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void setString(int index, String value) {
    try {
      prepstmt.setString(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setInt(int index, int value) {
    try {
      prepstmt.setInt(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setBoolean(int index, boolean value) {
    try {
      prepstmt.setBoolean(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setDate(int index, Date value) throws SQLException {
    try {
      prepstmt.setDate(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setLong(int index, long value) throws SQLException {
    try {
      prepstmt.setLong(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setFloat(int index, float value) throws SQLException {
    try {
      prepstmt.setFloat(index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setBinaryStream(int index, InputStream in, int length) throws SQLException {
    try {
      prepstmt.setBinaryStream(index, in, length);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearParameters() throws SQLException {
    try {
      prepstmt.clearParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PreparedStatement getPreparedStatement() {
    return prepstmt;
  }

  public Statement getStatement() {
    return stmt;
  }


  public ResultSet executeQuery() {
    try {
      if (prepstmt != null) {
        return prepstmt.executeQuery();
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public ResultSet executeQuery(String sql) {
    try {
      if (stmt != null) {
        return stmt.executeQuery(sql);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public void executeUpdate(String sql) {
    try {
      if (stmt != null) {
        stmt.executeUpdate(sql);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void executeUpdate() {
    try {
      if (prepstmt != null) {
        prepstmt.executeUpdate();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      if (prepstmt != null) {
        prepstmt.close();
        prepstmt = null;
      }
      if (stmt != null) {
        stmt.close();
        stmt = null;
      }

      conn.close();
      conn = null;

      // System.out.println("Closed!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
