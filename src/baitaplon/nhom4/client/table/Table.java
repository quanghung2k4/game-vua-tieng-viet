
package baitaplon.nhom4.client.table;

import baitaplon.nhom4.client.model.ModelPlayer;
import baitaplon.nhom4.client.model.ModelProfile;
import baitaplon.nhom4.client.swing.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Table extends JTable{
    public Table(){
        setShowHorizontalLines(false);
        setShowVerticalLines(false);
        getTableHeader().setBorder(null);

        setGridColor(Color.white);
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(true);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, 
                    Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                TableHeader header = new TableHeader(value+"");
          
                if(column == 2 || column == 3){
                    header.setHorizontalAlignment(JLabel.CENTER);
                } else {
                    header.setHorizontalAlignment(JLabel.LEFT);
                }
                return header;
            }
            
        });
        
        setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
               
                // Căn giữa cho cột score (2) và status (3)
                if(column == 2 || column == 3){
                    setHorizontalAlignment(JLabel.CENTER);
                }
                
                // Xử lý màu sắc cho cột status (column 3)
                if(column == 3){
                    com.setFont(com.getFont().deriveFont(Font.BOLD, 16f)); // in đậm + size 16
                    String status = value != null ? value.toString().trim() : "";
                    
                    if("Online".equalsIgnoreCase(status)){
                        com.setForeground(new Color(0, 200, 83)); // xanh lá sáng (green)
                    } else if("Offline".equalsIgnoreCase(status)){
                        com.setForeground(new Color(255, 0, 0)); // đỏ tươi (red)
                    } else {
                        com.setForeground(new Color(255, 165, 0)); // cam (orange) cho các trạng thái khác
                    }
                } else {
                    // Các cột khác dùng màu xám
                    com.setForeground(new Color(102,102,102));
                }
                if(value instanceof ModelProfile){
                    ModelProfile data = (ModelProfile) value;
                    Profile cell = new Profile(data);
                    if(isSelected){
                        cell.setBackground(new Color(239,244,255));
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                    return cell;
                } else {
                    setBorder(noFocusBorder);
                    if(isSelected){
                        com.setBackground(new Color(239,244,255));
                    } else {
                        com.setBackground(Color.WHITE);
                    }
                    return com;
                }
                
                
            }
            
        });
    }
    public void addRow(Object []row){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }
    public void fixTable(JScrollPane scroll) {
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        scroll.setBorder(new EmptyBorder(5, 10, 5, 10));
    }
}
    
