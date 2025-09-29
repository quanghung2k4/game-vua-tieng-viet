
package baitaplon.nhom4.client.component;

import baitaplon.nhom4.client.event.EventMenuSelected;
import baitaplon.nhom4.client.model.ModelMenu;
import baitaplon.nhom4.client.swing.MenuItem;
import baitaplon.nhom4.client.swing.ScrollBarCustom;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author ADMIN
 */
public class Menu extends javax.swing.JPanel {

    private EventMenuSelected event;
    private final MigLayout layout;

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap,fillx,insets 0,gap 0", "[fill]", "[]");
        panel.setLayout(layout);
    }

    public EventMenuSelected getEvent() {
        return event;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void initMenuItem() {
        addMenu(new ModelMenu(resizeIcon("/baitaplon/nhom4/client/icon/home.png", 25, 25), "Trang chủ"));
        addMenu(new ModelMenu(resizeIcon("/baitaplon/nhom4/client/icon/rank.png", 25,25), "BXH điểm"));
        addMenu(new ModelMenu(resizeIcon("/baitaplon/nhom4/client/icon/ranking-star.png", 25,25), "BXH số trận thắng"));
        addMenu(new ModelMenu(resizeIcon("/baitaplon/nhom4/client/icon/time-past.png", 25,25), "Lịch sử đấu"));
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, event, panel.getComponentCount()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        name2 = new baitaplon.nhom4.client.component.Name();

        setBackground(new java.awt.Color(204, 204, 204));

        sp.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        sp.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setForeground(new java.awt.Color(255, 255, 255));
        panel.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(name2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(name2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D grap = (Graphics2D) g;
        grap.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gra = new GradientPaint(0, 0, new Color(127, 229, 255), getWidth(), 0, new Color(127, 229, 255));
        grap.setPaint(gra);
        grap.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g); 
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private baitaplon.nhom4.client.component.Name name2;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
