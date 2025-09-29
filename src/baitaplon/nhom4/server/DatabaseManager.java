
package baitaplon.nhom4.server;

import com.sun.jdi.connect.spi.Connection;

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/penalty_shootout";
    private static final String USER = "root"; 
    private static final String PASSWORD = "123456";

    private Connection conn;

    public DatabaseManager() {
//        conn = DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
