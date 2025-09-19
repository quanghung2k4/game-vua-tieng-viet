
package baitaplon.nhom4.client.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class TableHeader extends JLabel{

    public TableHeader(String text) {
        super(text);
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(new Font("SansSerif",1,16));
        setForeground(new Color(102,102,102));
        
        setBorder(new EmptyBorder(10,5,10,5));
         setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D grap = (Graphics2D) g;
        grap.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        grap.setColor(new Color(230,230,230));
//        grap.setColor(Color.white);
        super.paintComponent(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }


    
    
}
