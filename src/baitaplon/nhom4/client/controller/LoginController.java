
package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.component.HomeForm;
import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.view.Login;
import baitaplon.nhom4.client.view.DashBoard;
import javax.swing.SwingUtilities;

public class LoginController {
    private final Login view;
    private final TCPClient client;
    private boolean isLoggingIn = false;

    public LoginController(Login view, TCPClient client) {
        this.view = view;
        this.client = client;
        this.view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        // Kiểm tra xem đang trong quá trình đăng nhập không
        if (isLoggingIn) {
            return;
        }
        String username = view.getUsername();
        String password = view.getPassword();

        // Validation input cơ bản
        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Kiểm tra độ dài username
        if (username.length() < 3 || username.length() > 20) {
            view.showMessage("Tên đăng nhập phải từ 3-20 ký tự!");
            return;
        }

        // Kiểm tra độ dài password
        if (password.length() < 4) {
            view.showMessage("Mật khẩu phải có ít nhất 4 ký tự!");
            return;
        }

        // Bắt đầu quá trình đăng nhập
        startLoginProcess(username, password);
    }

    private void startLoginProcess(String username, String password) {
        isLoggingIn = true;
        
        // Hiển thị trạng thái đang đăng nhập
        view.setLoginButtonEnabled(false);
        view.setInputFieldsEnabled(false);
        view.setLoginButtonText("Đang đăng nhập...");

        // Chạy đăng nhập trong thread riêng để không block UI
        new Thread(() -> {
            try {
                // Tạo request message với timestamp
                String content = username.trim() + "|" + password;
                MessageModel request = new MessageModel("request_login", content);
                
                // Gửi request đến server
                client.sendMessage(request);

                // Xử lý response
//                handleLoginResponse(response, username);
                
            } catch (Exception ex) {
                // Xử lý lỗi kết nối
                SwingUtilities.invokeLater(() -> {
                    handleConnectionError(ex);
                });
            } finally {
                // Reset UI state
                SwingUtilities.invokeLater(this::resetLoginState);
            }
        }).start();
    }

    private void handleConnectionError(Exception ex) {
        String errorMessage = "Lỗi kết nối server!";
        
        if (ex instanceof java.net.ConnectException) {
            errorMessage = "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng!";
        } else if (ex instanceof java.net.SocketTimeoutException) {
            errorMessage = "Kết nối timeout. Server có thể đang quá tải!";
        }
        
        view.showMessage(errorMessage);
    }


    public void handleLoginResponse(MessageModel response) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                view.showMessage("Không nhận được phản hồi từ server!");
                resetLoginState();
                return;
            }

            String content = response.getContent();
            System.out.println("Phản hồi login: " + content);

            switch (content) {
                case "OK":
                    view.showMessage("Đăng nhập thành công!");
                    openDashboard(view.getUsername());
                    break;
                case "INVALID_CREDENTIALS":
                    view.showMessage("Sai tài khoản hoặc mật khẩu!");
                    break;
                case "USER_ALREADY_ONLINE":
                    view.showMessage("Tài khoản đang đăng nhập ở nơi khác!");
                    break;
                default:
                    view.showMessage("Lỗi: " + content);
                    break;
            }

            resetLoginState();
        });
    }


    private void resetLoginState() {
        isLoggingIn = false;
        view.setLoginButtonEnabled(true);
        view.setInputFieldsEnabled(true);
        view.setLoginButtonText("Đăng nhập");
    }

    // Getter để kiểm tra trạng thái đăng nhập
    public boolean isLoggingIn() {
        return isLoggingIn;
    }
    
    /**
     * Mở Dashboard sau khi đăng nhập thành công
     */
    private void openDashboard(String username) {
        // Tạo Dashboard với username và client
        DashBoard dashboard = new DashBoard(username, client);
        DashBoardController dashBoardController = new DashBoardController(username, dashboard,client);
        dashboard.setDashBoardController(dashBoardController);

        client.setDashBoardController(dashBoardController);
        // Set title cho window
        dashboard.setTitleWithUsername();
        
        // Hiển thị Dashboard
        dashboard.setVisible(true);
        
        // Đóng form Login
        view.dispose();
        
        System.out.println("Chuyển đến Dashboard cho user: " + username);
    }
}
