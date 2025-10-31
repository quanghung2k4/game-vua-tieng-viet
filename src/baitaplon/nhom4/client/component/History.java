
package baitaplon.nhom4.client.component;

import baitaplon.nhom4.client.controller.DashBoardController;
import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.model.ModelHistory;
import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.table.EventAction;
import java.awt.Image;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;

public class History extends javax.swing.JPanel {
    
    private TCPClient tcpClient;
    private DashBoardController dashBoardController;
    public History() throws Exception {
        initComponents();
        table1.setShowGrid(false); // bỏ tất cả grid line
        tcpClient = TCPClient.getInstance();
        dashBoardController = DashBoardController.getInstance();
        dashBoardController.setHistoryForm(this);
        sendMessage();
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
        jLabel1.setText("Lịch sử");

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên", "Thời gian bắt đầu", "Kết quả"
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
        EventAction eventAction = new EventAction() {
            @Override
            public void invite(ModelPlayer player) {
                
            }
        };
        ModelHistory x2 = new ModelHistory(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25),"Jony Nguyen","19:15 9/10/2025","Thắng");
        ModelHistory x3 = new ModelHistory(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25),"Jony Nguyen","19:15 9/10/2025","Hòa");
        table1.addRow((Object[]) x2.toRowTable(eventAction));
        table1.addRow((Object[]) x3.toRowTable(eventAction));
    }
    
    public void initTableData(List<ModelHistory> historyList){
        table1.fixTable(jScrollPane1);
        EventAction eventAction = new EventAction() {
            @Override
            public void invite(ModelPlayer player) {
                
            }
        };
        for (ModelHistory h:historyList){
            h.setIcon(resizeIcon("/baitaplon/nhom4/client/icon/circle_user.png", 25, 25));
            table1.addRow((Object[]) h.toRowTable(eventAction));
        }
    }
    
     private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    private void sendMessage() throws IOException, ClassNotFoundException {
        MessageModel mess = new MessageModel("request_history",dashBoardController.getCurrentUsername());
        tcpClient.sendMessage(mess);
    }
}
