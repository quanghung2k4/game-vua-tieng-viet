package baitaplon.nhom4.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/ltm1";
    private static final String USER = "root";
    private static final String PASSWORD = "1912";

    private DatabaseManager() {}

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver MySQL JDBC!");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
