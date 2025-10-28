package baitaplon.nhom4.client.view;

import baitaplon.nhom4.client.component.Header;
import baitaplon.nhom4.client.component.History;
import baitaplon.nhom4.client.component.HomeForm;
import baitaplon.nhom4.client.component.MainForm;
import baitaplon.nhom4.client.component.Menu;
import baitaplon.nhom4.client.component.RankScore;
import baitaplon.nhom4.client.component.RankWin;
import baitaplon.nhom4.client.controller.DashBoardController;
import baitaplon.nhom4.client.controller.LeaderboardController;
import baitaplon.nhom4.client.controller.LoginController;
import baitaplon.nhom4.client.event.EventMenuSelected;
import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class DashBoard extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainForm main;
    private HomeForm homeForm;
    private RankScore leaderboardForm;
    private LeaderboardController leaderboardController;
    private int mouseX, mouseY;

    private String username;
    private TCPClient client;
    private DashBoardController controller;

    public DashBoard() {
        this("Guest", null);
    }

    public DashBoard(String username) {
        this(username, null);
    }

    public DashBoard(String username, TCPClient client) {
        this.username = username;
        this.client = client;

        
        initComponents();
        this.setLocationRelativeTo(null); // đặt form ra giữa màn hình

        getRootPane().setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY, 1, true));
        mouseLister();
        init();
        addWindowListener();
    }

    private void init() {
        layout = new MigLayout("fill", "0[]0[100%,fill]0", "0[fill,top]0");
        background.setLayout(layout);
        menu = new Menu();
        header = new Header();
        main = new MainForm();
        homeForm = new HomeForm();
        leaderboardForm = new RankScore();
        setIconImage(
                new javax.swing.ImageIcon(
                        getClass().getResource("/baitaplon/nhom4/client/icon/logo.png")
                ).getImage()
        );
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int menuIndex) {
                System.out.println("Menu selected: " + menuIndex);
                // 0: Trang chủ, 1: Bảng xếp hạng, 2: Lịch sử đấu
                if (menuIndex == 0) {
                    main.showForm(homeForm);
                    initializePlayerListRefresh();
                } else if (menuIndex == 1) {
                    main.showForm(leaderboardForm);
                    loadLeaderboard();
                } else if (menuIndex == 2) {
                    main.showForm(new History());
                } else {
                    main.showForm(homeForm);
                    initializePlayerListRefresh();
                }
            }
        });

        menu.initMenuItem(); //menu
        
        // Thêm listener cho nút đăng xuất
        menu.addLogoutListener(e -> handleLogout());
        
        background.add(menu, "w 200!, spany 2");
        background.add(header, "h 50!, wrap");
        background.add(main, "w 100%, h 100%");

        // Set username trong header
        header.setUsername(username);

        // Mặc định hiển thị Trang chủ
        main.showForm(homeForm);
        initializePlayerListRefresh();
    }

    /**
     * Khởi tạo và bắt đầu việc lấy danh sách người chơi từ server
     */
    private void initializePlayerListRefresh() {
        if (client != null && controller == null) {
            controller = new DashBoardController(this, client, homeForm);
            controller.startPlayerListRefresh();
            System.out.println("Đã khởi tạo DashBoardController và bắt đầu lấy danh sách người chơi");
        } else if (controller != null) {
            // Nếu đã có controller, chỉ cần đảm bảo nó đang chạy
            if (!controller.isRunning()) {
                controller.startPlayerListRefresh();
            }
        }
    }

    private void loadLeaderboard() {
        if (client != null) {
            if (leaderboardController == null) {
                leaderboardController = new LeaderboardController(leaderboardForm, client);
            }
            leaderboardController.loadLeaderboard();
        }
    }

    /**
     * Thêm window listener để dừng refresh và đăng xuất khi đóng window
     */
    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (controller != null) {
                    controller.stopPlayerListRefresh();
                    System.out.println("Đã dừng refresh danh sách người chơi");
                }
                // Gửi request logout khi đóng window
                sendLogoutRequest();
            }
        });
    }
    
    /**
     * Xử lý khi người dùng bấm nút đăng xuất
     */
    private void handleLogout() {
        sendLogoutRequest();
        backToLogin();
    }
    
    /**
     * Gửi request logout đến server
     */
    private void sendLogoutRequest() {
        if (client != null && username != null) {
            new Thread(() -> {
                try {
                    MessageModel request = new MessageModel("request_logout", username);
                    MessageModel response = (MessageModel) client.sendMessage(request);
                    if (response != null && "return_logout".equals(response.getType())) {
                        System.out.println("Đăng xuất thành công: " + response.getContent());
                    }
                } catch (Exception ex) {
                    System.err.println("Lỗi khi gửi request logout: " + ex.getMessage());
                }
            }).start();
        }
    }
    
    /**
     * Quay về màn hình đăng nhập
     */
    private void backToLogin() {
        // Đóng dashboard
        this.dispose();
        
        // Mở login
        Login login = new Login(client);
        new LoginController(login, client);
        login.setVisible(true);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game vua tiếng việt");
        setResizable(false);

        background.setBackground(new java.awt.Color(231, 231, 231));
        background.setOpaque(true);

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane background;
    // End of variables declaration//GEN-END:variables

    private void mouseLister() {
        // Gắn sự kiện kéo để di chuyển cửa sổ
        background.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        background.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
    }

    // Getter cho username
    public String getUsername() {
        return username;
    }

    // Method để set title với username
    public void setTitleWithUsername() {
        this.setTitle("Game Vua Tiếng Việt - " + username);
    }
}
