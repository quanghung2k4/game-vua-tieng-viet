package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.controller.GameScreenController;
import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.model.ModelResult;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.swing.Button;
import baitaplon.nhom4.shared.game.GameStartDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameScreen extends javax.swing.JFrame {

    private Thread countDownThread;
    private int time;

    private final TCPClient tcpClient;
    private GameScreenController controller;

    private String myUsername, opponentUsername;
    public GameScreen(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        initComponents();
        initController();
        this.tcpClient.setActiveGameScreen(this);

        // chặn tắt cửa sổ đột ngột -> confirm
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });

        // Bắt đầu timer tổng (ví dụ 120 giây) nếu bạn đang dùng
        countDown(120);
    }

    private void initController() {
        controller = new GameScreenController(tcpClient);
        controller.attachTo(jLayeredPane1);
    }

    // Được DashBoardController gọi khi TCPClient nhận "game_start"
    public void startGame(GameStartDTO dto) {
        myUsername = dto.getPlayer1();
        opponentUsername = dto.getPlayer2();

        myName.setText(dto.getPlayer1DisplayName());
        opponentName.setText(dto.getPlayer2DisplayName());

        // Hiển thị countdown 3-2-1 theo thời điểm server gửi và render câu đầu
        controller.startWithBatch(dto.getBatch(), dto.getStartAtEpochMs(), dto.getCountdownSeconds(), this);
    }

    // TCPClient sẽ gọi khi nhận "game_end"
    // message content dự kiến: "winnerUsername|loserUsername|reason"
    public void handleGameEnd(String content) {
        try {
            String[] parts = (content == null ? "" : content).split("\\|");
            String winner = parts.length > 0 ? parts[0] : "";
            String loser  = parts.length > 1 ? parts[1] : "";
            String reason = parts.length > 2 ? parts[2] : "";

            boolean iWin = myUsername != null && myUsername.equals(winner);
            String verdict = iWin ? "Win" : "Loss";

            // Dừng đồng hồ nếu có
            if (countDownThread != null && countDownThread.isAlive()) {
                time = -1;
            }

            // Tính điểm hiện tại (giữ nguyên nhịp tăng điểm của bạn)
            ModelResult result = new ModelResult(
                    myName.getText(), myScore.getText(),
                    opponentName.getText(), opponentScore.getText(),
                    verdict
            );
            JOptionPane.showMessageDialog(this,
                    (iWin ? "Bạn thắng! " : "Bạn thua! ") + "(" + reason + ")",
                    "Kết thúc ván", JOptionPane.INFORMATION_MESSAGE);

            GameResult gameResult = new GameResult(result);
            gameResult.setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Nhấn nút Thoát hoặc đóng cửa sổ
    private void handleExit() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thoát không? Đối thủ sẽ thắng ván này.",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) {
            // Gửi forfeit lên server để kết thúc ván và chấm thắng cho đối thủ
            try {
                String content = (myUsername != null ? myUsername : "") + "|" + (opponentUsername != null ? opponentUsername : "");
                tcpClient.sendMessage(new baitaplon.nhom4.client.model.MessageModel("game_forfeit", content));
            } catch (Exception ignore) {}
            // Đóng ngay giao diện của người thoát
            this.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code - giữ nguyên các thành phần đã có; bổ sung xử lý nút Thoát">
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

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setMaximumSize(new java.awt.Dimension(327, 327));
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setText("");
        label1.setFont(new java.awt.Font("SansSerif", 0, 30));
        jLayeredPane1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 400, 40, 60));

        btnCheck.setBackground(new java.awt.Color(0, 255, 255));
        btnCheck.setForeground(new java.awt.Color(255, 255, 255));
        btnCheck.setText("Kiểm tra");
        btnCheck.setFont(new java.awt.Font("SansSerif", 1, 18));
        btnCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCheckMouseClicked(evt);
            }
        });
        jLayeredPane1.add(btnCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 490, 180, 40));

        btnExit.setBackground(new java.awt.Color(0, 255, 255));
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Thoát");
        btnExit.setFont(new java.awt.Font("SansSerif", 0, 14));
        btnExit.addActionListener(e -> handleExit());
        jLayeredPane1.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, 30));

        jPanel3.setOpaque(false);

        myScore.setFont(new java.awt.Font("SansSerif", 1, 24));
        myScore.setForeground(new java.awt.Color(255, 255, 255));
        myScore.setText("0");

        opponentScore.setFont(new java.awt.Font("SansSerif", 1, 24));
        opponentScore.setForeground(new java.awt.Color(255, 255, 255));
        opponentScore.setText("0");

        timeDown.setFont(new java.awt.Font("SansSerif", 1, 18));
        timeDown.setForeground(new java.awt.Color(255, 255, 255));
        timeDown.setText("0:30");

        myName.setFont(new java.awt.Font("SansSerif", 1, 14));
        myName.setForeground(new java.awt.Color(51, 51, 51));
        myName.setText("Bạn");

        opponentName.setFont(new java.awt.Font("SansSerif", 1, 14));
        opponentName.setForeground(new java.awt.Color(51, 51, 51));
        opponentName.setText("Đối thủ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(myScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                                .addComponent(myName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(timeDown, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opponentName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/state.png")));
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

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/baitaplon/nhom4/client/icon/background-game.png")));

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
    }// </editor-fold>

    private void btnCheckMouseClicked(java.awt.event.MouseEvent evt) {
        if (controller.checkAnswer()) {
            int score = Integer.parseInt(myScore.getText());
            myScore.setText("" + (score + 1));
            controller.nextWord();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Sai rồi! Hãy thử lại.");
        }
    }

    // Timer tổng thể (giữ logic cũ)
    private void countDown(int t) {
        this.time = t;
        countDownThread = new Thread(() -> {
            while (time >= 0) {
                int minutes = time / 60;
                int seconds = time % 60;
                timeDown.setText(String.format("%d:%02d", minutes, seconds));
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                if(time % 5 == 0) opponentScore.setText(""+(Integer.parseInt(opponentScore.getText()) + 1));
                time--;
            }
            if (time < 0) {
                String ans = "Hòa";
                int mScore = Integer.parseInt(myScore.getText());
                int oScore = Integer.parseInt(opponentScore.getText());
                if(mScore > oScore) ans = "Win";
                else if(mScore < oScore) ans = "Loss";
                ModelResult result = new ModelResult(myName.getText(),myScore.getText(),
                        opponentName.getText(),opponentScore.getText(), ans);
                GameResult gameResult = new GameResult(result);
                gameResult.setVisible(true);
                this.dispose();
            }
        });
        countDownThread.start();
    }

    // ==== autogenerated fields ====
    private javax.swing.JLabel background;
    private Button btnCheck;
    private Button btnExit;
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
}