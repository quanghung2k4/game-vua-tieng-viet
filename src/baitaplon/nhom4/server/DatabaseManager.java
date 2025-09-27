
package baitaplon.nhom4.server;

import com.sun.jdi.connect.spi.Connection;

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/penalty_shootout";
    private static final String USER = "root"; // Thay bằng user MySQL của bạn
    private static final String PASSWORD = "123456"; // Thay bằng password MySQL của bạn

    private Connection conn;

    public DatabaseManager() {
//        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
