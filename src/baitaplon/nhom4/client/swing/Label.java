package baitaplon.nhom4.client.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

/**
 *
 * @author ADMIN
 */
public class Label extends JLabel {

    private Color backgroundColor;
    private int cornerRadius = 10;
    private Point origin;
    private Point target;
    private boolean atTarget = false;
    private Timer animationTimer;
    private long animationStartTime;
    private int animationDuration = 200; // ms
    private int fps = 60;

    public Label() {
        super();
        setOpaque(false);
        System.out.println("Label constructor gọi 1 lần duy nhất");

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        backgroundColor = Color.WHITE;
        setForeground(Color.BLACK);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (origin == null) {
                    origin = getLocation(); // lưu vị trí gốc
                }
                if (target == null) {
                    target = new Point(origin.x + 50, origin.y - 200); // ví dụ len tren 100px
                }
                System.out.println("Label repaint, atTarget=" + atTarget + " location=" + getLocation());

                animateMove(atTarget ? origin : target);
            }
        });
    }

    private void animateMove(Point endPos) {
        Point startPos = getLocation();
        animationStartTime = System.currentTimeMillis();

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(1000 / fps, ev -> {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            float fraction = Math.min(1f, (float) elapsed / animationDuration);

            int newX = (int) (startPos.x + fraction * (endPos.x - startPos.x));
            int newY = (int) (startPos.y + fraction * (endPos.y - startPos.y));
            setLocation(newX, newY);

            if (fraction >= 1f) {
                ((Timer) ev.getSource()).stop();
                atTarget = !atTarget;
            }
        });
        animationTimer.start();
    }

    public Label(String text) {
        this();
        setText(text);
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }

}
