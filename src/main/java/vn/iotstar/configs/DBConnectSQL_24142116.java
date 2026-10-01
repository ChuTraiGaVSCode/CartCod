package vn.iotstar.configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectSQL_24142116 {

	private static final String SERVER = "localhost";
	private static final String PORT = "1433";
	private static final String DB_NAME = "LTWeb_De04";
	private static final String USER = "sa";
	private static final String PASSWORD = "60021051";

	private static final String URL = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";databaseName=" + DB_NAME
			+ ";encrypt=true;trustServerCertificate=true";

	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Không tìm thấy SQL Server JDBC driver", e);
		}
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void main(String[] args) {
		try (Connection c = getConnection()) {
			System.out.println("Kết nối thành công: " + c.getCatalog());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
