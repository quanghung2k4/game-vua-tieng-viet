
package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.controller.LoginController;
import baitaplon.nhom4.client.network.TCPClient;
import java.net.Socket;

/**
 *
 * @author ADMIN
 */
public class MainClient {
    public static void main(String[] args) {
        Login login =new Login();
        TCPClient client = new TCPClient("localhost", 12345);
        new LoginController(login, client);
        login.setVisible(true);
    }
}
