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

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 180));
        panel.add(label, BorderLayout.CENTER);
        setContentPane(panel);
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