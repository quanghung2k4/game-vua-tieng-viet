
package baitaplon.nhom4.client.network;

import baitaplon.nhom4.client.model.MessageModel;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    private final String host;
    private final int port;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public Object sendMessage(MessageModel message) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream  out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream  in = new ObjectInputStream (socket.getInputStream())) {
            out.writeObject(message);      // gửi Message
            out.flush();
            return in.readObject();    // đọc Object trả về
        }
    }
}
