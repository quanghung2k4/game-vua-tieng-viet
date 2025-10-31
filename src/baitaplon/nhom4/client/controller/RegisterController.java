package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.view.Register;
import baitaplon.nhom4.client.view.Login;
import javax.swing.SwingUtilities;

public class RegisterController {
    private final Register view;
    private final TCPClient client;
    private boolean isRegistering = false;

    public RegisterController(Register view, TCPClient client) {
        this.view = view;
        this.client = client;
        this.view.addRegisterListener(e -> handleRegister());
        // Đăng ký controller để nhận phản hồi từ TCPClient
        this.client.setRegisterController(this);
    }

    private void handleRegister() {
        // Kiểm tra xem đang trong quá trình đăng ký không
        if (isRegistering) {
            return;
        }

        String username = view.getUsername();
        String password = view.getPassword();
        String fullName = view.getFullName();

        // Validation input cơ bản
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
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

        // Kiểm tra độ dài fullname
        if (fullName.length() < 2 || fullName.length() > 50) {
            view.showMessage("Họ và tên phải từ 2-50 ký tự!");
            return;
        }

        // Bắt đầu quá trình đăng ký
        startRegisterProcess(username, password, fullName);
    }

    private void startRegisterProcess(String username, String password, String fullName) {
        isRegistering = true;
        
        // Hiển thị trạng thái đang đăng ký
        view.setRegisterButtonEnabled(false);
        view.setInputFieldsEnabled(false);
        view.setRegisterButtonText("Đang đăng ký...");

        // Chạy đăng ký trong thread riêng để không block UI
        new Thread(() -> {
            try {
                // Tạo request message với timestamp
                String content = username.trim() + "|" + password + "|" + fullName.trim();
                MessageModel request = new MessageModel("request_register", content);
                
                // Gửi request đến server
                 client.sendMessage(request);
                
                // Phản hồi sẽ được nhận bất đồng bộ qua TCPClient.listenServer()
                
            } catch (Exception ex) {
                // Xử lý lỗi kết nối
                SwingUtilities.invokeLater(() -> {
                    handleConnectionError(ex);
                });
            } finally {
                // Reset UI state
                SwingUtilities.invokeLater(() -> {
                    resetRegisterState();
                });
            }
        }).start();
    }

    public void handleRegisterResponse(MessageModel response) {
        SwingUtilities.invokeLater(() -> {
            if (response == null) {
                view.showMessage("Không nhận được phản hồi từ server!");
                return;
            }

            String responseContent = response.getContent();
            
            if ("OK".equals(responseContent)) {
                // Đăng ký thành công
                view.showMessage("Đăng ký thành công!");
                
                // Chuyển về form Login
                goBackToLogin();
                
            } else if ("USERNAME_EXISTS".equals(responseContent)) {
                view.showMessage("Tên đăng nhập đã tồn tại!");
                
            } else if ("INVALID_USERNAME".equals(responseContent)) {
                view.showMessage("Tên đăng nhập không hợp lệ!");
                
            } else if ("INVALID_PASSWORD".equals(responseContent)) {
                view.showMessage("Mật khẩu không hợp lệ!");
                
            } else if ("SERVER_ERROR".equals(responseContent)) {
                view.showMessage("Lỗi server. Vui lòng thử lại sau!");
                
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

    private void resetRegisterState() {
        isRegistering = false;
        view.setRegisterButtonEnabled(true);
        view.setInputFieldsEnabled(true);
        view.setRegisterButtonText("Đăng ký");
    }

    // Getter để kiểm tra trạng thái đăng ký
    public boolean isRegistering() {
        return isRegistering;
    }
    
    /**
     * Chuyển về form Login sau khi đăng ký thành công
     */
    private void goBackToLogin() {
        Login login = new Login(client);
        new LoginController(login, client);
        login.setVisible(true);
        view.dispose();
        
        System.out.println("Chuyển về Login sau khi đăng ký thành công");
    }
    
    
}
