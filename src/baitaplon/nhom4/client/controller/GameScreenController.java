package baitaplon.nhom4.client.controller;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.client.network.TCPClient;
import baitaplon.nhom4.shared.game.WordBatchDTO;
import baitaplon.nhom4.shared.game.WordChallengeDTO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameScreenController {

    private final TCPClient tcpClient;
    private JLayeredPane layeredPane;

    private JPanel selectedLettersPanel;
    private JPanel shuffledLettersPanel;

    private final Deque<WordChallengeDTO> queue = new ArrayDeque<>(32);
    private WordChallengeDTO current;
    private final List<String> selectedLetters = new ArrayList<>();
    private final List<JButton> letterButtons = new ArrayList<>();

    public GameScreenController(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        this.tcpClient.setGameScreenController(this);
    }

    public void attachTo(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;

        selectedLettersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        selectedLettersPanel.setOpaque(false);
        shuffledLettersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        shuffledLettersPanel.setOpaque(false);

        layeredPane.add(selectedLettersPanel,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 700, 80));
        layeredPane.add(shuffledLettersPanel,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 700, 80));
        layeredPane.setLayer(selectedLettersPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.setLayer(shuffledLettersPanel, JLayeredPane.PALETTE_LAYER);
    }

    // Gọi khi bắt đầu ván hoặc khi hết batch
    public void requestBatch() {
//        try {
//            tcpClient.sendMessage(new MessageModel("request_word_batch", (String) null));
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(layeredPane, "Không gửi được yêu cầu batch: " + e.getMessage());
//        }
    }

    // Được TCPClient gọi khi server trả batch
    public void handleWordBatch(WordBatchDTO batch) {
        queue.clear();
        queue.addAll(batch.getChallenges());
        nextWord();
    }

    public boolean checkAnswer() {
        if (current == null) return false;
        String ans = String.join("", selectedLetters).trim().toLowerCase();
        return ans.equals(current.getOriginalWord().trim().toLowerCase());
    }

    public void nextWord() {
        if (queue.isEmpty()) {
            requestBatch();  // xin thêm 30 từ khi đã dùng hết
            return;
        }
        current = queue.pollFirst();
        selectedLetters.clear();
        renderShuffledLetters();
        renderSelectedLetters();
    }

    // ===== UI render =====

    private void renderShuffledLetters() {
        shuffledLettersPanel.removeAll();
        letterButtons.clear();

        for (String letter : current.getShuffledLetters()) {
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
        label.setBackground(new Color(0, 200, 200, 100));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return label;
    }

    public String getCurrentWord() {
        return current != null ? current.getOriginalWord() : "";
    }
}