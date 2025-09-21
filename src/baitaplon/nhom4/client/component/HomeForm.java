package baitaplon.nhom4.client.component;

import baitaplon.nhom4.client.main.GameScreen;
import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.model.ModelProfile;
import baitaplon.nhom4.client.table.EventAction;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class HomeForm extends javax.swing.JPanel {

    private EventAction eventAction;

    public HomeForm() {
        initComponents();
        table1.setShowGrid(false); // bỏ tất cả grid line
        initTableData();
        tableMouseListen();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new baitaplon.nhom4.client.table.Table();

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setText("Người chơi");

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên", "Điểm", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        table1.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setResizable(false);
            table1.getColumnModel().getColumn(1).setResizable(false);
            table1.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private baitaplon.nhom4.client.table.Table table1;
    // End of variables declaration//GEN-END:variables

    private void initTableData() {
        table1.fixTable(jScrollPane1);
        eventAction = new EventAction() {
            @Override
            public void invite(ModelPlayer player) {
                GameScreen dashBoard = new GameScreen(player);
                dashBoard.setVisible(true);
            }
        };
        ModelPlayer x1 = new ModelPlayer(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25), "Jony A", 20, "Online");
        ModelPlayer x2 = new ModelPlayer(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25), "Jony B", 20, "Busy");
        ModelPlayer x3 = new ModelPlayer(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25), "Jony C", 20, "Offline");
        table1.addRow((Object[]) x1.toRowTable1(eventAction));
        table1.addRow((Object[]) x2.toRowTable1(eventAction));
        table1.addRow((Object[]) x3.toRowTable1(eventAction));
    }

    private void tableMouseListen() {
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow(); // lấy dòng được chọn
                if (row >= 0) {
                    // duyệt tất cả cột để tìm ModelPlayer
                    int colCount = table1.getColumnCount();
                  
                    ModelProfile profile = (ModelProfile) table1.getValueAt(row, 0);
                    ModelPlayer player;
                    player = new ModelPlayer(profile.getIcon(),profile.getName(), (int)table1.getValueAt(row, 1),(String)table1.getValueAt(row,2));
                    if (player != null) {
                        // gọi invite
                        eventAction.invite(player);
                    }
                }
            }

        });
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}
