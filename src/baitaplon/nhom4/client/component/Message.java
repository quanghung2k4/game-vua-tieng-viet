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
import java.util.logging.Level;
import java.util.logging.Logger;


public class Message extends javax.swing.JPanel {

    /**
     * Creates new form Message
     */
    private String mess;
    private MessageModel messageModel;
    private TCPClient tCPClient;
    public Message(String mess) {
        this.mess = mess;
        initComponents();
        lbInvite.setText(mess);
        setOpaque(false);
    }
    public Message(String mess, boolean showButton) {
        this.mess = mess;
        initComponents();
        lbInvite.setText(mess);
        button1.setVisible(false);
        setOpaque(false);
    }
    public Message(String mess,MessageModel messageModel) {
        this.mess = mess;
        initComponents();
        lbInvite.setText(mess);
        this.messageModel = messageModel;
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

        lbInvite = new javax.swing.JLabel();
        button1 = new baitaplon.nhom4.client.swing.Button();

        setBackground(new java.awt.Color(255, 255, 255));

        lbInvite.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lbInvite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbInvite.setText("Đang mời người chơi ...");
        lbInvite.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lbInvite.setName(""); // NOI18N

        button1.setBackground(new java.awt.Color(102, 255, 255));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Hủy");
        button1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button1MouseClicked(evt);
            }
        });
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(199, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(197, 197, 197))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbInvite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(lbInvite, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_button1ActionPerformed

    private void button1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button1MouseClicked
        tCPClient = TCPClient.getInstance();
        try {
            tCPClient.sendMessage(messageModel);
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        GlassPanePopup.closePopupLast();
    }//GEN-LAST:event_button1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private baitaplon.nhom4.client.swing.Button button1;
    private javax.swing.JLabel lbInvite;
    // End of variables declaration//GEN-END:variables
}
