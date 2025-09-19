package baitaplon.nhom4.client.swing;

import baitaplon.nhom4.client.event.EventMenu;
import baitaplon.nhom4.client.event.EventMenuSelected;
import baitaplon.nhom4.client.model.ModelMenu;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author ADMIN
 */
public class MenuItem extends javax.swing.JPanel {

    private ModelMenu menu;
    private int index;
    private EventMenu event;
    private EventMenuSelected eventSelected;

    public MenuItem(ModelMenu menu, EventMenuSelected eventSelected, int index) {
        initComponents();
        this.menu = menu;
        this.index = index;
        this.eventSelected = eventSelected;
        setOpaque(false);
        setBorder(null); // bỏ viền của JPanel
        setLayout(new MigLayout("wrap,fillx,insets 0", "[fill]", "[fill,25!]0[fill,20!]"));
        MenuButton firstItem = new MenuButton(menu.getIcon(), "  "+menu.getMenuName());
        firstItem.setFont(new Font("SansSerif",Font.PLAIN,16));
        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventSelected.menuSelected(index);
            }
        });
        add(firstItem);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
