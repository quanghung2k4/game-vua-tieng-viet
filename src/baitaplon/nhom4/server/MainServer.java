
package baitaplon.nhom4.server;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private DatabaseManager dbManager;
    public MainServer(){
        try {
            serverSocket = new ServerSocket(PORT);
            dbManager = new DatabaseManager();
            System.out.println("Server đang chạy ở port " + PORT);
            
            //listen client
            while (true) {
                Socket client = serverSocket.accept();
                 ClientHandler clientHandler = new ClientHandler(client, this, dbManager);
                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new MainServer();
       
    }
}
