package baitaplon.nhom4.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    private static final String ALGORITHM = "SHA-256";

    /**
     * Hash mật khẩu bằng SHA-256 đơn giản (không có salt)
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedBytes = md.digest(password.getBytes());

            // Chuyển đổi thành chuỗi hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi khi hash mật khẩu", e);
        }
    }
}