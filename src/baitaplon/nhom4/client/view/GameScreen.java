package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.controller.GameScreenController;
import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.model.ModelResult;
import baitaplon.nhom4.client.network.TCPClient;

import javax.swing.*;

public class GameScreen extends javax.swing.JFrame {

    private Thread countDownThread;
    private int time;
    private static ModelPlayer player;

    // Controller gameplay
    private TCPClient tcpClient;
    private GameScreenController controller;

    public GameScreen(ModelPlayer player) {
//        this.tcpClient = tcpClient;
        this.player = player;
        initComponents();
        controller = new GameScreenController(this.tcpClient);
        controller.attachTo(jLayeredPane1);
        controller.requestBatch();
        countDown(120);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        label1 = new baitaplon.nhom4.client.swing.Label();
        btnCheck = new baitaplon.nhom4.client.swing.Button();
        btnExit = new baitaplon.nhom4.client.swing.Button();
        jPanel3 = new javax.swing.JPanel();
        myScore = new javax.swing.JLabel();
        opponentScore = new javax.swing.JLabel();
        timeDown = new javax.swing.JLabel();
        myName = new javax.swing.JLabel();
        opponentName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setMaximumSize(new java.awt.Dimension(327, 327));
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCheck.setBackground(new java.awt.Color(0, 255, 255));
        btnCheck.setForeground(new java.awt.Color(255, 255, 255));
        btnCheck.setText("Kiểm tra");
        btnCheck.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCheckMouseClicked(evt);
            }
        });
        btnCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckActionPerformed(evt);
            }
        });
        jLayeredPane1.add(btnCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 490, 180, 40));

        btnExit.setBackground(new java.awt.Color(0, 255, 255));
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Thoát");
        btnExit.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLayeredPane1.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, 30));

        jPanel3.setOpaque(false);

        myScore.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        myScore.setForeground(new java.awt.Color(255, 255, 255));
        myScore.setText("0");

        opponentScore.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        opponentScore.setForeground(new java.awt.Color(255, 255, 255));
        opponentScore.setText("0");

        timeDown.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        timeDown.setForeground(new java.awt.Color(255, 255, 255));
        timeDown.setText("0:30");

        myName.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        myName.setForeground(new java.awt.Color(51, 51, 51));
        myName.setText("Bạn");

        opponentName.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        opponentName.setForeground(new java.awt.Color(51, 51, 51));
        opponentName.setText("Join Nguyễn");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(myScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                                .addComponent(myName, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(timeDown, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opponentName, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(opponentScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(myName, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timeDown)
                                        .addComponent(opponentName, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(33, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(myScore)
                                .addGap(36, 36, 36))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(opponentScore)
                                .addGap(35, 35, 35))
        );

        jLayeredPane1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 6, -1, -1));

        jPanel2.setOpaque(false);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/state.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 32, -1, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/background-game.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLayeredPane1.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, -1));

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckActionPerformed
        // Không dùng
    }//GEN-LAST:event_btnCheckActionPerformed

    private void btnCheckMouseClicked(java.awt.event.MouseEvent evt) {
        if (controller.checkAnswer()) {
            int score = Integer.parseInt(myScore.getText());
            myScore.setText("" + (score + 1));
            JOptionPane.showMessageDialog(this, "✅ Chính xác! " + controller.getCurrentWord());
            controller.nextWord();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Sai rồi! Hãy thử lại.");
        }
    }//GEN-LAST:event_btnCheckMouseClicked

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new GameScreen(player).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private baitaplon.nhom4.client.swing.Button btnCheck;
    private baitaplon.nhom4.client.swing.Button btnExit;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private baitaplon.nhom4.client.swing.Label label1;
    private javax.swing.JLabel myName;
    private javax.swing.JLabel myScore;
    private javax.swing.JLabel opponentName;
    private javax.swing.JLabel opponentScore;
    private javax.swing.JLabel timeDown;
    // End of variables declaration//GEN-END:variables

    private void countDown(int t) {
        this.time = t;
        countDownThread = new Thread(() -> {
            while (time >= 0) {
                int minutes = time / 60;
                int seconds = time % 60;
                timeDown.setText(String.format("%d:%02d", minutes, seconds));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                if (time % 5 == 0) opponentScore.setText("" + (Integer.parseInt(opponentScore.getText()) + 1));
                time--;
            }
            if (time < 0) {
                String ans = "Hòa";
                int mScore = Integer.parseInt(myScore.getText());
                int oScore = Integer.parseInt(opponentScore.getText());
                if (mScore > oScore) {
                    ans = "Win";
                } else if (mScore < oScore) ans = "Loss";
                ModelResult result = new ModelResult(myName.getText(), myScore.getText(),
                        opponentName.getText(), opponentScore.getText(), ans);
                GameResult gameResult = new GameResult(result);
                gameResult.setVisible(true);
                this.dispose();
            }
        });
        countDownThread.start();
    }
}