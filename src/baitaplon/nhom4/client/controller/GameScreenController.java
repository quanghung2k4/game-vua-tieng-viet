package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.client.component.CountDownDialog;
import baitaplon.nhom4.client.view.GameScreen;
import baitaplon.nhom4.shared.game.WordBatchDTO;
import baitaplon.nhom4.shared.game.WordChallengeDTO;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreenController {

    private final TCPClient tcpClient;
    private JLayeredPane layeredPane;

    private JPanel selectedLettersPanel;
    private JPanel shuffledLettersPanel;

    private final Deque<WordChallengeDTO> queue = new ArrayDeque<>(64);
    private WordChallengeDTO current;
    private final java.util.List<String> selectedLetters = new java.util.ArrayList<>();
    private final java.util.List<JButton> letterButtons = new java.util.ArrayList<>();
    private GameScreen gameScreen;

    public GameScreenController(TCPClient tcpClient, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.tcpClient = tcpClient;
    }

    public void attachTo(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;

        selectedLettersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        selectedLettersPanel.setOpaque(false);

        shuffledLettersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        shuffledLettersPanel.setOpaque(false);

        // Vị trí: hàng sắp xếp (trên), hàng lựa chọn (dưới)
        layeredPane.add(selectedLettersPanel,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 800, 80));
        layeredPane.add(shuffledLettersPanel,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 800, 80));
        layeredPane.setLayer(selectedLettersPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.setLayer(shuffledLettersPanel, JLayeredPane.PALETTE_LAYER);
    }

    public void startWithBatch(WordBatchDTO batch, long startAtEpochMs, int fallbackSeconds, JFrame owner) {
        queue.clear();
        queue.addAll(batch.getChallenges());

        long now = System.currentTimeMillis();
        long msLeft = Math.max(0, startAtEpochMs - now);
        int seconds = (int)Math.ceil(msLeft / 1000.0);
        if (seconds <= 0) seconds = Math.max(1, fallbackSeconds);

        CountDownDialog.show(owner, seconds, () -> SwingUtilities.invokeLater(() -> {
            gameScreen.countDown(120);
            nextWord();
        }));
    }

    private String normalizeNoSpace(String s) {
        if (s == null) return "";
        return s.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    public boolean checkAnswer() {
        if (current == null) return false;
        String ans = normalizeNoSpace(String.join("", selectedLetters));
        String target = normalizeNoSpace(current.getOriginalWord());
        return ans.equals(target);
    }

    // Dọn ngay dòng đã sắp xếp
    public void clearSelectedLetters() {
        selectedLetters.clear();
        selectedLettersPanel.removeAll();
        selectedLettersPanel.revalidate();
        selectedLettersPanel.repaint();

        // Kích hoạt lại toàn bộ nút chữ cái (phòng khi người chơi dựng sai trước đó)
        for (JButton btn : letterButtons) {
            btn.setEnabled(true);
            btn.setBackground(new Color(0, 255, 255));
        }
    }

    public void nextWord() {
        if (queue.isEmpty()) {
            JOptionPane.showMessageDialog(layeredPane, "Hết câu hỏi!");
            return;
        }
        System.out.print(queue.peekFirst().getOriginalWord() + " ");
        current = queue.pollFirst();
        System.out.print(queue.peekFirst().getOriginalWord() + " ");
        clearSelectedLetters();
        renderShuffledLetters();
    }

    private void renderShuffledLetters() {
        shuffledLettersPanel.removeAll();
        letterButtons.clear();

        // Bỏ khoảng trắng trong danh sách hiển thị ở hàng dưới
        java.util.List<String> letters = current.getShuffledLetters()
                .stream()
                .filter(ch -> ch != null && !ch.trim().isEmpty())
                .collect(Collectors.toList());

        for (String letter : letters) {
            JButton btn = makeLetterButton(letter);
            btn.addActionListener(e -> {
                if (btn.isEnabled()) {
                    selectedLetters.add(letter);
                    btn.setEnabled(false);
                    btn.setBackground(new Color(150, 150, 150));
                    renderSelectedLetters();
                }
            });
            letterButtons.add(btn);
            shuffledLettersPanel.add(btn);
        }
        shuffledLettersPanel.revalidate();
        shuffledLettersPanel.repaint();
    }

    private void renderSelectedLetters() {
        selectedLettersPanel.removeAll();
        for (int i = 0; i < selectedLetters.size(); i++) {
            final int idx = i;
            JLabel label = makeSelectedLabel(selectedLetters.get(i));
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    removeLetterAt(idx);
                }
            });
            selectedLettersPanel.add(label);
        }
        selectedLettersPanel.revalidate();
        selectedLettersPanel.repaint();
    }

    private void removeLetterAt(int index) {
        if (index < 0 || index >= selectedLetters.size()) return;
        String removed = selectedLetters.remove(index);
        for (JButton btn : letterButtons) {
            if (!btn.isEnabled() && btn.getText().equals(removed)) {
                btn.setEnabled(true);
                btn.setBackground(new Color(0, 255, 255));
                break;
            }
        }
        renderSelectedLetters();
    }

    private JButton makeLetterButton(String letter) {
        JButton btn = new JButton(letter);
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setBackground(new Color(0, 255, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    private JLabel makeSelectedLabel(String letter) {
        JLabel label = new JLabel(letter, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(50, 50));
        label.setOpaque(true);
        label.setBackground(new Color(0, 200, 200, 200));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return label;
    }
}