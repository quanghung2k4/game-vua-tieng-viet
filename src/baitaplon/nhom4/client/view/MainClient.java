
package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.controller.LoginController;
import baitaplon.nhom4.client.network.TCPClient;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ADMIN
 */
public class MainClient {
    public static void main(String[] args) throws IOException {
        // Set Look and Feel cho toàn bộ ứng dụng
        setLookAndFeel();

        // Tạo TCPClient một lần duy nhất và tái sử dụng

        TCPClient client = new TCPClient("26.244.192.199", 3636);

        // Tạo Login form với client
        Login login = new Login(client);
        LoginController loginController = new LoginController(login, client);
        client.setLoginController(loginController);
        login.setVisible(true);
    }

    private static void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
