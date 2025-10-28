package baitaplon.nhom4.client.component;

import baitaplon.nhom4.client.model.LeaderboardRow;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankScore extends JPanel {

    private JPanel contentPanel;
    private JScrollPane scrollPane;

    public RankScore() {
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setOpaque(true);

        JLabel title = new JLabel("Bảng xếp hạng", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Tạo card bo tròn hiển thị 1 người chơi
     */
    private JPanel createPlayerCard(int rank, LeaderboardRow row) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor;
                if (rank == 1) bgColor = new Color(255, 215, 0);       // vàng
                else if (rank == 2) bgColor = new Color(192, 192, 192); // bạc
                else if (rank == 3) bgColor = new Color(205, 127, 50);  // đồng
                else bgColor = new Color(245, 245, 245);

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
            }
        };

        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(640, 80));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel rankLabel = new JLabel("#" + rank, SwingConstants.CENTER);
        rankLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        rankLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        card.add(rankLabel, gbc);

        JLabel nameLabel = new JLabel(row.getDisplayName(), SwingConstants.LEFT);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        card.add(nameLabel, gbc);

        JLabel pointsLabel = new JLabel("Điểm: " + row.getTotalPoints(), SwingConstants.CENTER);
        pointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        card.add(pointsLabel, gbc);

        JLabel matchesLabel = new JLabel("Trận: " + row.getTotalMatches(), SwingConstants.CENTER);
        matchesLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        card.add(matchesLabel, gbc);

        JLabel winsLabel = new JLabel("Thắng: " + row.getTotalWins(), SwingConstants.CENTER);
        winsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 4;
        gbc.weightx = 0.2;
        card.add(winsLabel, gbc);

        return card;
    }

    /**
     * Gán danh sách người chơi lên bảng xếp hạng
     */
    public void setLeaderboard(List<LeaderboardRow> rows) {
        contentPanel.removeAll();

        if (rows == null || rows.isEmpty()) {
            JLabel empty = new JLabel("Chưa có dữ liệu xếp hạng", SwingConstants.CENTER);
            empty.setFont(new Font("SansSerif", Font.ITALIC, 16));
            empty.setForeground(Color.GRAY);
            contentPanel.add(Box.createVerticalStrut(20));
            contentPanel.add(empty);
        } else {
            int rank = 1;
            for (LeaderboardRow row : rows) {
                JPanel card = createPlayerCard(rank++, row);
                contentPanel.add(card);
                contentPanel.add(Box.createVerticalStrut(10)); // khoảng cách giữa các card
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void clearTable() {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
