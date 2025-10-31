package baitaplon.nhom4.client.view;

import java.awt.Color;
import baitaplon.nhom4.client.network.TCPClient;

public class Register extends javax.swing.JFrame {

    private TCPClient client;

    public Register() {
        this(null);
    }

    public Register(TCPClient client) {
        this.client = client;
        initComponents();
        getContentPane().setBackground(new Color(255, 255, 255));
        this.setLocationRelativeTo(null); // đặt form ra giữa màn hình
        getRootPane().setBorder(new javax.swing.border.LineBorder(Color.lightGray, 1, true)); // border
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtUserName = new baitaplon.nhom4.client.swing.TextField();
        btnRegister = new baitaplon.nhom4.client.swing.Button();
        txtPassword = new baitaplon.nhom4.client.swing.PasswordField();
        txtFullName = new baitaplon.nhom4.client.swing.TextField();
        btnBackToLogin = new baitaplon.nhom4.client.swing.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Đăng ký tài khoản");

        txtUserName.setForeground(new java.awt.Color(51, 51, 51));
        txtUserName.setDisabledTextColor(new java.awt.Color(85, 85, 85));
        txtUserName.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        txtUserName.setLabelText("Tên đăng nhập");
        txtUserName.setLineColor(new java.awt.Color(11, 206, 255));

        btnRegister.setBackground(new java.awt.Color(11, 206, 255));
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("Đăng ký");
        btnRegister.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnRegister.setPreferredSize(new java.awt.Dimension(118, 40));
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        txtPassword.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtPassword.setLabelText("Mật khẩu");

        txtFullName.setForeground(new java.awt.Color(51, 51, 51));
        txtFullName.setDisabledTextColor(new java.awt.Color(85, 85, 85));
        txtFullName.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        txtFullName.setLabelText("Họ và tên");
        txtFullName.setLineColor(new java.awt.Color(11, 206, 255));

        btnBackToLogin.setBackground(new java.awt.Color(108, 117, 125));
        btnBackToLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnBackToLogin.setText("← Quay lại");
        btnBackToLogin.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnBackToLogin.setPreferredSize(new java.awt.Dimension(118, 40));
        btnBackToLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackToLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBackToLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(150, 150, 150))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBackToLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        pack();
    }

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic đăng ký sẽ được xử lý bởi RegisterController
    }

    private void btnBackToLoginActionPerformed(java.awt.event.ActionEvent evt) {
        goBackToLogin();
    }

    private baitaplon.nhom4.client.swing.Button btnBackToLogin;
    private baitaplon.nhom4.client.swing.Button btnRegister;
    private javax.swing.JLabel jLabel1;
    private baitaplon.nhom4.client.swing.PasswordField txtPassword;
    private baitaplon.nhom4.client.swing.TextField txtFullName;
    private baitaplon.nhom4.client.swing.TextField txtUserName;

    public String getUsername() {
        return txtUserName.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getFullName() {
        return txtFullName.getText().trim();
    }

    public void addRegisterListener(java.awt.event.ActionListener l) {
        for (java.awt.event.ActionListener listener : btnRegister.getActionListeners()) {
            btnRegister.removeActionListener(listener);
        }
        btnRegister.addActionListener(l);
    }

    public void showMessage(String msg) {
        javax.swing.JOptionPane.showMessageDialog(this, msg);
    }

    public void setRegisterButtonEnabled(boolean enabled) {
        btnRegister.setEnabled(enabled);
    }

    public void setRegisterButtonText(String text) {
        btnRegister.setText(text);
    }

    public void clearForm() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        txtUserName.requestFocus();
    }

    public void setInputFieldsEnabled(boolean enabled) {
        txtUserName.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        txtFullName.setEnabled(enabled);
    }

    private void goBackToLogin() {
        Login login = new Login(client);
        new baitaplon.nhom4.client.controller.LoginController(login, client);
        login.setVisible(true);
        this.dispose();
    }

    public TCPClient getClient() {
        return client;
    }

    public void setClient(TCPClient client) {
        this.client = client;
    }
}
