package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.component.Message;
import baitaplon.nhom4.client.controller.DashBoardController;
import baitaplon.nhom4.client.controller.GameScreenController;
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

    private TCPClient tcpClient;
    private GameScreenController controller;

    private String hosting, myUsername, opponentUsername;
    private boolean isEndGame = false;
    // Label hiệu ứng +1 điểm
    private JLabel plusOneLabel;

    public GameScreen(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        initComponents();
        initController();
        this.tcpClient.setActiveGameScreen(this);
        // Chặn tắt cửa sổ đột ngột -> confirm
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });

    }

    public GameScreen() {
        initComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });

        // Timer tổng
        countDown(120);
    }

    private void initController() {
        controller = new GameScreenController(tcpClient, this);
        controller.attachTo(jLayeredPane1);
    }

    // DashBoardController gọi khi TCPClient nhận "game_start"
    public void startGame(GameStartDTO dto) {
        hosting = dto.getInviter();
        myUsername = dto.getPlayer1();
        opponentUsername = dto.getPlayer2();

        myName.setText(dto.getPlayer1DisplayName());
        opponentName.setText(dto.getPlayer2DisplayName());

        // Count down 3-2-1 theo đồng hồ server rồi render câu đầu
        controller.startWithBatch(dto.getBatch(), dto.getStartAtEpochMs(), dto.getCountdownSeconds(), dto.getTotalTimeGameplay(), this);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameScreen gs = new GameScreen();          // không cần TCPClient
            gs.setLocationRelativeTo(null);
            gs.setVisible(true);
//            gs.startPreview();                          // chạy demo UI
        });
    }

    // TCPClient sẽ gọi khi nhận "game_end"; content: "winnerUsername|loserUsername|reason"
    public void handleGameEnd(String content) {
        if(isEndGame) return;
        try {
            isEndGame = true;
            String[] parts = (content == null ? "" : content).split("\\|");
            String myScoreMess = parts.length > 0 ? parts[0] : "";
            String opponentScoreMess = parts.length > 1 ? parts[1] : "";
            String reason = parts.length > 2 ? parts[2] : "";

            String verdict="";
            int p1Sc = Integer.parseInt(myScoreMess);
            int p2Sc = Integer.parseInt(opponentScoreMess);
            if(p1Sc > p2Sc){ verdict = "Win"; }
            else if (p2Sc > p1Sc){ verdict = "Lose"; }
            else { verdict = "Draw"; }

            String message = "";
            System.out.println(verdict+" "+reason);
            if(reason.equals("game_forfeit")){
                if(verdict.equals("Win")) {
                    message = "Đối thủ đã đầu hàng!";
                }
                else {
                    message = "Bạn đã đầu hàng!";
                }
            }
            if(reason.equals("finish_game")){
                if(verdict.equals("Win")) {
                    message = "Bạn đã thắng!";
                }
                else if(verdict.equals("Draw")) {
                    message = "Hai người chơi hòa ván này";
                }
                else {
                    message = "Bạn đã thua!";
                }
            }
            if(reason.equals("disconnect")){
                message = "Đối thủ đã rời đi hoặc mất kết nối";
            }
            System.out.println(message);
            ModelResult result = new ModelResult(
                    myName.getText(), myScoreMess,
                    opponentName.getText(), opponentScoreMess,
                    verdict
            );
            result.setMyUsername(myUsername);
            result.setoUsername(opponentUsername);
            JOptionPane.showMessageDialog(this, message, "Kết thúc ván", JOptionPane.INFORMATION_MESSAGE);

            GameResult gameResult = new GameResult(result);
            
            gameResult.setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleOpponentScored(String content) {
        try {
            int score = Integer.parseInt(opponentScore.getText());
            opponentScore.setText("" + (score + 1));
            showPlusOneUnder(opponentScore);
        } catch (Exception ignored) {}
    }

    private void handleExit() {
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thoát game không? Đối thủ sẽ thắng ván này.",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) {
            try {
                isEndGame = true;
                String content = (myUsername != null ? myUsername : "") + "|" + (opponentUsername != null ? opponentUsername : "");
                tcpClient.sendMessage(new baitaplon.nhom4.client.model.MessageModel("player_out", content));
            } catch (Exception ignore) {}
            this.dispose();
        }
    }

    private void handleForfeit(){
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thoát không? Đối thủ sẽ thắng ván này.",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) {
            try {
                String content = (myUsername != null ? myUsername : "") + "|" + (opponentUsername != null ? opponentUsername : "")
                                +myScore.getText() +"|"+ opponentScore.getText()+ "|game_forfeit";
                tcpClient.sendMessage(new baitaplon.nhom4.client.model.MessageModel("finish_game", content));
            } catch (Exception ignore) {}
        }
    }

    private void showPlusOneUnder(JLabel scoreLabel) {
        final int w = 40, h = 22;

        // Tính vị trí ngay dưới ô điểm trong toạ độ của jLayeredPane1
        Point p = SwingUtilities.convertPoint(scoreLabel.getParent(),
                scoreLabel.getLocation(),
                jLayeredPane1);
        int x = p.x + (scoreLabel.getWidth() - w) / 2;
        int y = p.y + scoreLabel.getHeight() + 6;

        if (plusOneLabel == null) {
            plusOneLabel = new JLabel("+1", SwingConstants.CENTER);
            plusOneLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            plusOneLabel.setForeground(new Color(0, 255, 0));
            plusOneLabel.setOpaque(false);
            jLayeredPane1.add(plusOneLabel,
                    new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, w, h));
            jLayeredPane1.setLayer(plusOneLabel, JLayeredPane.POPUP_LAYER);
        } else {
            jLayeredPane1.add(plusOneLabel,
                    new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, w, h));
        }

        plusOneLabel.setVisible(true);
        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();

        Timer t = new Timer(900, e -> plusOneLabel.setVisible(false));
        t.setRepeats(false);
        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        label1 = new baitaplon.nhom4.client.swing.Label();     // placeholder (bỏ)
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
        btnExit.addActionListener(e -> handleForfeit());
        jLayeredPane1.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, 30));

        jPanel3.setOpaque(false);

        myScore.setFont(new java.awt.Font("SansSerif", 1, 24));
        myScore.setForeground(new java.awt.Color(255, 255, 255));
        myScore.setText("0");
        myScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        opponentScore.setFont(new java.awt.Font("SansSerif", 1, 24));
        opponentScore.setForeground(new java.awt.Color(255, 255, 255));
        opponentScore.setText("0");
        opponentScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        timeDown.setFont(new java.awt.Font("SansSerif", 1, 18));
        timeDown.setForeground(new java.awt.Color(255, 255, 255));
        timeDown.setText("2:00");

        myName.setFont(new java.awt.Font("SansSerif", 1, 14));
        myName.setForeground(new java.awt.Color(51, 51, 51));
        myName.setText("Bạn");;
        myName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        opponentName.setFont(new java.awt.Font("SansSerif", 1, 14));
        opponentName.setForeground(new java.awt.Color(51, 51, 51));
        opponentName.setText("Đối thủ");
        opponentName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(myScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(myName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(timeDown, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(opponentName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(opponentScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(myName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timeDown)
                                        .addComponent(opponentName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(33, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(myScore)
                                .addGap(31, 31, 31))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(opponentScore)
                                .addGap(31, 31, 31))
        );

        // Thay vì add theo GroupLayout mặc định của NetBeans, setBounds sẽ được đồng bộ trong alignHeaderOverState()
        jLayeredPane1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 8, -1, -1));

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
        jLayeredPane1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 26, -1, -1));

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
            // Xóa ngay phần đã sắp xếp ở bên trên
            controller.clearSelectedLetters();

            // Cộng điểm và hiện +1 ngay dưới ô điểm của người ghi điểm (bên mình)
            int score = Integer.parseInt(myScore.getText());
            myScore.setText("" + (score + 1));
            showPlusOneUnder(myScore);
            // Từ tiếp theo
            controller.nextWord();
            try {
                if (tcpClient != null) {
                    String content = (myUsername != null ? myUsername : "") + "|" + (opponentUsername != null ? opponentUsername : "");
                    tcpClient.sendMessage(new baitaplon.nhom4.client.model.MessageModel("game_word_correct", content));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Sai rồi! Hãy thử lại.");
        }
    }

    public void countDown(int t) {
        this.time = t;
        countDownThread = new Thread(() -> {
            while (time >= 0 && !isEndGame) {
                int minutes = time / 60;
                int seconds = time % 60;
                timeDown.setText(String.format("%d:%02d", minutes, seconds));
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                time--;
            }
            if (time < 0) {
                try {
                    if(hosting.equals(myUsername)){
//                        String result = "3";
//                        int mScore = Integer.parseInt(myScore.getText());
//                        int oScore = Integer.parseInt(opponentScore.getText());
//                        System.out.println(mScore +" "+ oScore);
//                        if(mScore > oScore) result = "1";
//                        if(oScore > mScore) result = "2";
                        String content = myUsername +"|"+ opponentUsername +"|" + myScore.getText() +"|"+ opponentScore.getText() + "|" + "finish_game";
                        tcpClient.sendMessage(new baitaplon.nhom4.client.model.MessageModel("finish_game", content));
                    }
                } catch (Exception ignore) {}
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
    private baitaplon.nhom4.client.swing.Label label1; // placeholder, không dùng
    private javax.swing.JLabel myName;
    private javax.swing.JLabel myScore;
    private javax.swing.JLabel opponentName;
    private javax.swing.JLabel opponentScore;
    private javax.swing.JLabel timeDown;
}