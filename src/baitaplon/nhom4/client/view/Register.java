package baitaplon.nhom4.client.view;

import java.awt.Color;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import baitaplon.nhom4.client.network.TCPClient;

public class Register extends javax.swing.JFrame {

    private TCPClient client;

    public Register() {
        this(null);
    }
    
    public Register(TCPClient client) {
        this.client = client;
        initComponents();
        getContentPane().setBackground(new Color(255,255,255));
        this.setLocationRelativeTo(null); // đặt form ra giữa màn hình
        getRootPane().setBorder(new javax.swing.border.LineBorder(Color.lightGray, 1, true));//border
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtUserName = new baitaplon.nhom4.client.swing.TextField();
        btnRegister = new baitaplon.nhom4.client.swing.Button();
        txtPassword = new baitaplon.nhom4.client.swing.PasswordField();
        txtFullName = new baitaplon.nhom4.client.swing.TextField();
        btnSelectAvatar = new baitaplon.nhom4.client.swing.Button();
        lblAvatarPreview = new javax.swing.JLabel();
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

        btnSelectAvatar.setBackground(new java.awt.Color(108, 117, 125));
        btnSelectAvatar.setForeground(new java.awt.Color(255, 255, 255));
        btnSelectAvatar.setText("Chọn Avatar");
        btnSelectAvatar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnSelectAvatar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAvatarActionPerformed(evt);
            }
        });

        lblAvatarPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvatarPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/circle_user.png"))); // NOI18N
        lblAvatarPreview.setText("Avatar");
        lblAvatarPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lblAvatarPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnBackToLogin.setBackground(new java.awt.Color(108, 117, 125));
        btnBackToLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnBackToLogin.setText("← Quay lại");
        btnBackToLogin.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtFullName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addComponent(lblAvatarPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBackToLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSelectAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnSelectAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAvatarPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBackToLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // Logic đăng ký sẽ được xử lý bởi RegisterController
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnSelectAvatarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAvatarActionPerformed
        selectAvatar();
    }//GEN-LAST:event_btnSelectAvatarActionPerformed

    private void btnBackToLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackToLoginActionPerformed
        goBackToLogin();
    }//GEN-LAST:event_btnBackToLoginActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private baitaplon.nhom4.client.swing.Button btnBackToLogin;
    private baitaplon.nhom4.client.swing.Button btnRegister;
    private baitaplon.nhom4.client.swing.Button btnSelectAvatar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAvatarPreview;
    private baitaplon.nhom4.client.swing.PasswordField txtPassword;
    private baitaplon.nhom4.client.swing.TextField txtFullName;
    private baitaplon.nhom4.client.swing.TextField txtUserName;
    // End of variables declaration//GEN-END:variables

    // Lấy username
    public String getUsername() {
        return txtUserName.getText().trim();
    }

    // Lấy password
    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }
    
    // Lấy fullname
    public String getFullName() {
        return txtFullName.getText().trim();
    }
    
    // Lấy avatar path
    public String getAvatarPath() {
        return avatarPath;
    }

    public void addRegisterListener(java.awt.event.ActionListener l) { 
        // Xóa tất cả ActionListener cũ trước khi thêm mới
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
    
    // Method để clear form sau khi đăng ký thành công
    public void clearForm() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        setDefaultAvatar();
        txtUserName.requestFocus();
    }

    public void setInputFieldsEnabled(boolean enabled) {
        txtUserName.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        txtFullName.setEnabled(enabled);
        btnSelectAvatar.setEnabled(enabled);
    }
    
    // Biến để lưu đường dẫn avatar
    private String avatarPath = "";
    
    // Method để chọn avatar
    private void selectAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            avatarPath = selectedFile.getAbsolutePath();
            
            // Hiển thị preview avatar
            try {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(avatarPath);
                // Resize image để fit với label
                java.awt.Image img = icon.getImage().getScaledInstance(
                    lblAvatarPreview.getWidth(), 
                    lblAvatarPreview.getHeight(), 
                    java.awt.Image.SCALE_SMOOTH);
                lblAvatarPreview.setIcon(new javax.swing.ImageIcon(img));
            } catch (Exception e) {
                showMessage("Không thể tải hình ảnh!");
            }
        }
    }
    
    // Method để set avatar mặc định
    private void setDefaultAvatar() {
        avatarPath = "";
        lblAvatarPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/circle_user.png")));
    }
    
    // Method để quay lại form Login
    private void goBackToLogin() {
        Login login = new Login(client);
        new baitaplon.nhom4.client.controller.LoginController(login, client);
        login.setVisible(true);
        this.dispose();
    }
    
    // Getter cho client
    public TCPClient getClient() {
        return client;
    }
    
    // Setter cho client
    public void setClient(TCPClient client) {
        this.client = client;
    }
}
