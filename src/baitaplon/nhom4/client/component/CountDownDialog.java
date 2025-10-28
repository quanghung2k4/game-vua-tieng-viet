package baitaplon.nhom4.client.component;

import javax.swing.*;
import java.awt.*;

public class CountDownDialog extends JDialog {

    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private volatile boolean running = false;

    private CountDownDialog(JFrame owner) {
        super(owner, true);
        setUndecorated(true);
        setSize(200, 200);
        setLocationRelativeTo(owner);
        label.setFont(new Font("SansSerif", Font.BOLD, 72));
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Bo góc
                g2.dispose();
            }
        };
        panel.setBackground(new Color(0, 0, 0, 255));
        panel.add(label, BorderLayout.CENTER);
        setContentPane(panel);

        // Bo tròn dialog
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
    }


    public static void show(JFrame owner, int seconds, Runnable onDone) {
        CountDownDialog dlg = new CountDownDialog(owner);
        new Thread(() -> {
            dlg.running = true;
            dlg.setVisible(true);
        }).start();

        new Thread(() -> {
            int s = Math.max(1, seconds);
            try {
                while (s > 0) {
                    final int val = s;
                    SwingUtilities.invokeLater(() -> dlg.label.setText(String.valueOf(val)));
                    Thread.sleep(1000);
                    s--;
                }
            } catch (InterruptedException ignored) { }
            SwingUtilities.invokeLater(() -> {
                dlg.running = false;
                dlg.dispose();
                if (onDone != null) onDone.run();
            });
        }).start();
    }
}