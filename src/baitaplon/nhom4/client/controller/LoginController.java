
package baitaplon.nhom4.client.controller;

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
                MessageModel response = (MessageModel) client.sendMessage(request);
                
                // Xử lý response
                handleLoginResponse(response, username);
                
            } catch (Exception ex) {
                // Xử lý lỗi kết nối
                SwingUtilities.invokeLater(() -> {
                    handleConnectionError(ex);
                });
            } finally {
                // Reset UI state
                SwingUtilities.invokeLater(() -> {
                    resetLoginState();
                });
            }
        }).start();
    }

    private void handleLoginResponse(MessageModel response, String username) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                view.showMessage("Không nhận được phản hồi từ server!");
                return;
            }

            String responseContent = response.getContent();
            System.out.println(responseContent);
            
            if ("OK".equals(responseContent)) {
                // Đăng nhập thành công
                view.showMessage("Đăng nhập thành công! Chào mừng " + username);
                
                // Chuyển đến màn hình chính (Dashboard)
                openDashboard(username);
                
            } else if ("INVALID_CREDENTIALS".equals(responseContent)) {
                view.showMessage("Tên đăng nhập hoặc mật khẩu không đúng!");
                
            } else if ("USER_ALREADY_ONLINE".equals(responseContent)) {
                view.showMessage("Tài khoản này đã được đăng nhập ở nơi khác!");
                
            } else if ("SERVER_FULL".equals(responseContent)) {
                view.showMessage("Server đang quá tải. Vui lòng thử lại sau!");
                
            } else {
                // Hiển thị lỗi từ server
                view.showMessage("Lỗi: " + responseContent);
            }
        });
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
        
        // Set title cho window
        dashboard.setTitleWithUsername();
        
        // Hiển thị Dashboard
        dashboard.setVisible(true);
        
        // Đóng form Login
        view.dispose();
        
        System.out.println("Chuyển đến Dashboard cho user: " + username);
    }
}
