package baitaplon.nhom4.server;

import baitaplon.nhom4.client.model.MessageModel;
import baitaplon.nhom4.server.model.User;
import baitaplon.nhom4.server.service.GameResultService;
import baitaplon.nhom4.server.service.LeaderboardService;
import baitaplon.nhom4.server.service.UserService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    // map 1-1 giữa 2 username
    private static final Map<String, String> PAIRS = new ConcurrentHashMap<>();
    private static final Map<String, LocalDateTime> START_TIMES = new ConcurrentHashMap<>();
    private static Connection conn;

    public static void init(Connection connection) {
        conn = connection;
    }

    public static void registerPair(String u1, String u2) {
        if (u1 == null || u2 == null) return;
        PAIRS.put(u1, u2);
        PAIRS.put(u2, u1);

        LocalDateTime now = LocalDateTime.now();
        START_TIMES.put(u1, now);
        START_TIMES.put(u2, now);
    }

    public static String getOpponent(String user) {
        return user == null ? null : PAIRS.get(user);
    }
    public static LocalDateTime getStartTime(String user) {
        return START_TIMES.get(user);
    }

    public static void removePair(String u1, String u2) {
        if (u1 != null) {
            PAIRS.remove(u1);
            START_TIMES.remove(u1);
        }
        if (u2 != null) {
            PAIRS.remove(u2);
            START_TIMES.remove(u2);
        }
    }

    public static void endGame(String p1, String p2, String p1Score, String p2Score, String reason) {
        System.out.println(p1+" "+p2+" "+p1Score+" "+p2Score+" "+reason);
        try {
            ClientHandler player1 = MainServer.getClientHandlerByUserName(p1);
            ClientHandler player2 = MainServer.getClientHandlerByUserName(p2);
            if(reason.equals("disconnect")){
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", "10|0" + reason));
                recordGameResult(p1, p2, 10, 0);
            }
            if(reason.equals("game_forfeit")){
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", "10|0" + reason));
                if (player2 != null) player2.sendMessage(new MessageModel("game_end", "0|10" + reason));
                recordGameResult(p1, p2, 10, 0);
            }
            else{
                int p1Sc = Integer.parseInt(p1Score);
                int p2Sc = Integer.parseInt(p2Score);
                if (player1 != null) player1.sendMessage(new MessageModel("game_end", p1Score + "|" + p2Score + "|" + reason));
                if (player2 != null) player2.sendMessage(new MessageModel("game_end", p2Score + "|" + p1Score + "|" + reason));
                recordGameResult(p1, p2, p1Sc, p2Sc);
            }
            removePair(p1, p2);
        } catch (Exception ignored) {}
    }

    private static void recordGameResult(String p1, String p2, int  p1Sc, int p2Sc) {
        UserService userService = new UserService(conn);
        User user1 = userService.getUserByUserName(p1);
        User user2 = userService.getUserByUserName(p2);

        int user1Id = user1.getUserId();
        int user2Id = user2.getUserId();
        LocalDateTime playedAt = getStartTime(p1);
        GameResultService gameResultService = new GameResultService(conn);
        LeaderboardService leaderBoardService = new LeaderboardService(conn);

        if(user1Id < user2Id) gameResultService.createGameResult(user1Id, user2Id, p1Sc, p2Sc, playedAt);
        else gameResultService.createGameResult(user2Id, user1Id, p2Sc, p1Sc, playedAt);

        leaderBoardService.updateMatchResult(user1Id, p1Sc > p2Sc, p1Sc == p2Sc);
        leaderBoardService.updateMatchResult(user2Id, p2Sc > p1Sc, p1Sc == p2Sc);
    }

    public static void finishGame(String p1, String p2, String p1Score, String p2Score, String reason) {
        endGame(p1, p2, p1Score, p2Score, reason);
    }

    public static void playerOut(String loser) {
        String opp = getOpponent(loser);
        if (opp != null) {
            endGame(opp, loser, null, null, "disconnect");
        }
    }

    public static void notifyDisconnect(String user) {
        String opp = getOpponent(user);
        if (opp != null) {
            endGame(opp, user, null, null, "disconnect");
        }
    }
}