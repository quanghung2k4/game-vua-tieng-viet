
package baitaplon.nhom4.client.view;

import java.awt.Color;

public class Login extends javax.swing.JFrame {

    public Login() {
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
        btnLogin = new baitaplon.nhom4.client.swing.Button();
        txtPassword = new baitaplon.nhom4.client.swing.PasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Chào mừng trở lại !");

        txtUserName.setForeground(new java.awt.Color(51, 51, 51));
        txtUserName.setDisabledTextColor(new java.awt.Color(85, 85, 85));
        txtUserName.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        txtUserName.setLabelText("Tài khoản");
        txtUserName.setLineColor(new java.awt.Color(11, 206, 255));

        btnLogin.setBackground(new java.awt.Color(11, 206, 255));
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Đăng nhập");
        btnLogin.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        txtPassword.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtPassword.setLabelText("Mật khẩu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(355, Short.MAX_VALUE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(344, 344, 344))
            .addGroup(layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed

    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private baitaplon.nhom4.client.swing.Button btnLogin;
    private javax.swing.JLabel jLabel1;
    private baitaplon.nhom4.client.swing.PasswordField txtPassword;
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

    public void addLoginListener(java.awt.event.ActionListener l) { 
        // Xóa tất cả ActionListener cũ trước khi thêm mới
        for (java.awt.event.ActionListener listener : btnLogin.getActionListeners()) {
            btnLogin.removeActionListener(listener);
        }
        btnLogin.addActionListener(l); 
    }
    
    public void showMessage(String msg) { javax.swing.JOptionPane.showMessageDialog(this, msg); }

    public void setLoginButtonEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
    }
    
    public void setLoginButtonText(String text) {
        btnLogin.setText(text);
    }
    
    // Method để clear form sau khi đăng nhập thành công
    public void clearForm() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtUserName.requestFocus();
    }

    public void setInputFieldsEnabled(boolean enabled) {
        txtUserName.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
    }
}
