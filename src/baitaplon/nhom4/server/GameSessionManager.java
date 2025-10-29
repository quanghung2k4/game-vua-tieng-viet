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
//    public static void endGame(String winner, String loser, String reason) {
//        try {
//            String payload = (winner == null ? "" : winner) + "|" + (loser == null ? "" : loser) + "|" + (reason == null ? "" : reason);
//            MessageModel endMsg = new MessageModel("game_end", payload);
//
//            ClientHandler wH = MainServer.getClientHandlerByUserName(winner);
//            ClientHandler lH = MainServer.getClientHandlerByUserName(loser);
//
//            if (wH != null) wH.sendMessage(endMsg);
//            if (lH != null) lH.sendMessage(endMsg);
//
//            removePair(winner, loser);
//        } catch (Exception ignored) {}
//    }
    public static void endByOppOut(String winner, String loser, String reason){
        try {
            String payload = (winner == null ? "" : winner) + "|" + (loser == null ? "" : loser) + "|" + (reason == null ? "" : reason);
            MessageModel endMsg = new MessageModel("game_end", payload);

            ClientHandler wH = MainServer.getClientHandlerByUserName(winner);
            if (wH != null) wH.sendMessage(endMsg);

            removePair(winner, loser);
        } catch (Exception ignored) {}
    }
    public static void endGame(String p1, String p2, String pWin, String reason) {
        try {
            ClientHandler player1 = MainServer.getClientHandlerByUserName(p1);
            ClientHandler player2 = MainServer.getClientHandlerByUserName(p2);
            if(pWin == null){
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", "Win" + "|" + reason));
            }
            else{
                String result1="", result2="";
                if(pWin == "1"){ result1 = "Win"; result2 = "Lose"; }
                else if (pWin == "2"){ result1 = "Lose"; result2 = "Win"; }
                else if (pWin == "3"){ result1 = "Draw"; result2 = "Draw"; }
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", result1 + "|" + reason));
                if (player2 != null) player2.sendMessage(new MessageModel("game_end", result2 + "|" + reason));
            }
            removePair(p1, p2);
        } catch (Exception ignored) {}
    }

    public static void finishGame(String p1, String p2, String pWin, String reason) {
        endGame(p1, p2, pWin, reason);
    }

    public static void playerOut(String loser) {
        String opp = getOpponent(loser);
        if (opp != null) {
            endGame(opp, loser, null, "disconnect");
        }
    }

    public static void notifyDisconnect(String user) {
        String opp = getOpponent(user);
        if (opp != null) {
            endGame(opp, user, null, "disconnect");
        }
    }
}