package baitaplon.nhom4.client.network;

import baitaplon.nhom4.client.controller.DashBoardController;
import baitaplon.nhom4.client.controller.LoginController;
import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.view.GameScreen;
import baitaplon.nhom4.shared.game.GameStartDTO;

import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;

    private DashBoardController dashBoardController;
    private LoginController loginController;
    private volatile GameScreen activeGameScreen;
    private baitaplon.nhom4.client.controller.LeaderboardController leaderboardController;
    private static TCPClient tCPClient;

    public TCPClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        try {
            this.socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
            new Thread(this::listenServer).start();
            System.out.println("Kết nối server thành công: " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới server: " + e.getMessage());
            running = false;
        }
        tCPClient = this;
    }
    public static TCPClient getInstance(){
        return tCPClient;
    }



    public void sendMessage(MessageModel message) throws IOException, ClassNotFoundException {
        out.writeObject(message);      // gửi Message
        out.flush();
    }

    private void listenServer() {
        try {
            while (running) {
                MessageModel message = (MessageModel) in.readObject();
                System.out.println("Nhan duoc "+message.getType());
                switch (message.getType()){
                    case "return_login":
                        loginController.handleLoginResponse(message);
                        break;
                    case "return_get_players":
                        dashBoardController.handlePlayerListResponse(message);
                        break;
                    case "receive_invite":
                        dashBoardController.handleReceiveInvite(message);
                        break;
                    case "invite_error":
                        dashBoardController.handleInviteResponse(message);
                        break;
                    case "invite_result":
                        dashBoardController.handleInviteResponse(message);
                        break;
                    case "invite_cancel":
                        dashBoardController.handleInviteCancel(message);
                        break;
                    case "return_leaderboard":
                        if (leaderboardController != null) {
                            leaderboardController.handleLeaderboardResponse(message);
                        }
                        break;
                    case "game_start":
                        if (dashBoardController != null && message.getData() instanceof GameStartDTO) {
                            dashBoardController.handleGameStart((GameStartDTO) message.getData());
                        }
                        break;
                    case "opponent_scored":
                        if (activeGameScreen != null) {
                            activeGameScreen.handleOpponentScored(message.getContent());
                        }
                        break;
                    case "game_end":
                        if (activeGameScreen != null) {
                            activeGameScreen.handleGameEnd(message.getContent());
                        }
                        break;
                    case "refresh_player":
                        dashBoardController.fetchPlayerList();
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Mất kết nối tới server: " + e.getMessage());
            running = false;
        }
    }


    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }
    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }
    public void setLeaderboardController(baitaplon.nhom4.client.controller.LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }
    public void setActiveGameScreen(GameScreen screen) {
        this.activeGameScreen = screen;
    }


    public void disconnect() {
        running = false;
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
