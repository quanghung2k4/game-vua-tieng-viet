
package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.view.Login;

public class LoginController {
    private final Login view;
    private final TCPClient client;

    public LoginController(Login view, TCPClient client) {
        this.view = view;
        this.client = client;
        this.view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            MessageModel request = new MessageModel("request_login",username+"|"+password);
            MessageModel response = (MessageModel)client.sendMessage(request);

            if ("OK".equals(response.getContent())) {
                view.showMessage("Đăng nhập thành công!");
            } else {
                view.showMessage(response.getContent());
            }
        } catch (Exception ex) {
            view.showMessage("Lỗi kết nối server!");
        }
    }
}
