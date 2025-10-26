package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    // map 1-1 giữa 2 username
    private static final Map<String, String> PAIRS = new ConcurrentHashMap<>();

    public static void registerPair(String u1, String u2) {
        if (u1 == null || u2 == null) return;
        PAIRS.put(u1, u2);
        PAIRS.put(u2, u1);
    }

    public static String getOpponent(String user) {
        return user == null ? null : PAIRS.get(user);
    }

    public static void removePair(String u1, String u2) {
        if (u1 != null) PAIRS.remove(u1);
        if (u2 != null) PAIRS.remove(u2);
    }

    // Gửi game_end cho cả hai
    public static void endGame(String winner, String loser, String reason) {
        try {
            String payload = (winner == null ? "" : winner) + "|" + (loser == null ? "" : loser) + "|" + (reason == null ? "" : reason);
            MessageModel endMsg = new MessageModel("game_end", payload);

            ClientHandler wH = MainServer.getClientHandlerByUserName(winner);
            ClientHandler lH = MainServer.getClientHandlerByUserName(loser);

            if (wH != null) wH.sendMessage(endMsg);
            if (lH != null) lH.sendMessage(endMsg);

            removePair(winner, loser);
        } catch (Exception ignored) {}
    }

    public static void forfeit(String loser) {
        String opp = getOpponent(loser);
        if (opp != null) {
            endGame(opp, loser, "forfeit");
        }
    }

    public static void notifyDisconnect(String user) {
        String opp = getOpponent(user);
        if (opp != null) {
            endGame(opp, user, "disconnect");
        }
    }
}