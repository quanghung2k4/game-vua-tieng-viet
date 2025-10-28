/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package baitaplon.nhom4.client.component;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.swing.GlassPanePopup;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

/**
 *
 * @author ADMIN
 */
public class MessageChallage extends javax.swing.JPanel {

    /**
     * Creates new form Message
     */
    private TCPClient client;
    private String userNameSender;
    private String userNameReciever;
    private String displayReciever;

    public MessageChallage(String userNameSender, String  userNameReciever,String displaySender, TCPClient client) {
        this.userNameSender = userNameSender;
        this.userNameReciever = userNameReciever;
//        this.displayReciever = displayReciever;
        this.client = client;
        initComponents();
        lbinvited.setText(displaySender+" muốn chơi với bạn");
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        g2.dispose();
        super.paintComponent(g);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbinvited = new javax.swing.JLabel();
        btnAccept = new baitaplon.nhom4.client.swing.Button();
        btnDecline = new baitaplon.nhom4.client.swing.Button();

        setBackground(new java.awt.Color(255, 255, 255));

        lbinvited.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lbinvited.setText("Người chơi X muốn thách đấu bạn");

        btnAccept.setBackground(new java.awt.Color(102, 255, 255));
        btnAccept.setForeground(new java.awt.Color(255, 255, 255));
        btnAccept.setText("Chấp nhận");
        btnAccept.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnAccept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAcceptMouseClicked(evt);
            }
        });
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnDecline.setBackground(new java.awt.Color(204, 204, 204));
        btnDecline.setForeground(new java.awt.Color(255, 255, 255));
        btnDecline.setText("Từ chối");
        btnDecline.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        btnDecline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                    btnDeclineMouseClicked(evt);

            }
        });
        btnDecline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeclineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(lbinvited, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lbinvited, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnAcceptMouseClicked(java.awt.event.MouseEvent evt)  {//GEN-FIRST:event_btnAcceptMouseClicked
        GlassPanePopup.closePopupLast();
        try{
            System.out.println(userNameSender + "|" + userNameReciever + "|response_accept");
            client.sendMessage(new MessageModel("response_invite", userNameSender + "|" + userNameReciever + "|response_accept"));
        } catch (Exception e){

        }
    }//GEN-LAST:event_btnAcceptMouseClicked

    private void btnDeclineMouseClicked(java.awt.event.MouseEvent evt)  {//GEN-FIRST:event_btnDeclineMouseClicked
        // TODO add your handling code here:
        GlassPanePopup.closePopupLast();
        try {
            client.sendMessage(new MessageModel("response_invite", userNameSender + "|" + userNameReciever + "|response_reject"));
        } catch (Exception e){

        }

    }//GEN-LAST:event_btnDeclineMouseClicked

    private void btnDeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeclineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeclineActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private baitaplon.nhom4.client.swing.Button btnAccept;
    private baitaplon.nhom4.client.swing.Button btnDecline;
    private javax.swing.JLabel lbinvited;
    // End of variables declaration//GEN-END:variables
}
