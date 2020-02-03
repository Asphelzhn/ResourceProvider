package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SqlConst {
  private static int id = 0;

  private final String DB_URL =
      "jdbc:mysql://localhost:3306/source?useUnicode=true&characterEncoding=utf8&&useSSL=false";
  private final String NAME = "com.mysql.jdbc.Driver";
  private final String USER = "root";
  private final String PASS = "123456789";

  private Connection conn = null;
  private PreparedStatement pst = null;

  public SqlConst(String sql) {
    try {
      Class.forName(NAME);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      pst = conn.prepareStatement(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static int getId() {
    return id;
  }

  public static void setId(int id) {
    SqlConst.id = id + 1;
  }

  public Connection getConn() {
    return conn;
  }

  public PreparedStatement getPst() {
    return pst;
  }

  public void close() {
    try {
      this.pst.close();
      this.conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
