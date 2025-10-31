package baitaplon.nhom4.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class WordDictionary {

    private static final Set<String> DICT = new HashSet<>();
    private static final Random RAND = new Random();
    private static final String[] VIET_LETTERS = {
            "a","ă","â","b","c","d","đ","e","ê","g","h","i","k","l","m","n",
            "o","ô","ơ","p","q","r","s","t","u","ư","v","x","y"
    };

    private WordDictionary() {}

    public static void loadFromFile(String path) throws IOException {
        DICT.clear();
        File f = new File(path);
        if (!f.exists()) throw new FileNotFoundException("Dictionary file not found: " + path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String w = normalize(line);
                if (w.isEmpty()) continue;
                if (codePointLength(w) < 7) continue;
                DICT.add(w);
            }
        }
        System.out.println("✅ WordDictionary loaded: " + DICT.size() + " words from " + path);
    }

    public static boolean isValidWord(String word) {
        if (word == null) return false;
        return DICT.contains(normalize(word));
    }

    public static Set<String> getAllWords() {
        return Collections.unmodifiableSet(DICT);
    }

    public static Optional<String> pickRandomWord(int minLength, int maxLength) {
        List<String> filtered = DICT.stream()
                .filter(w -> {
                    int len = codePointLength(w);
                    return len >= minLength && len <= maxLength;
                })
                .collect(Collectors.toList());
        if (filtered.isEmpty()) return Optional.empty();
        return Optional.of(filtered.get(RAND.nextInt(filtered.size())));
    }

    /**
     * Tạo một WordChallenge mới với từ ngẫu nhiên
     * @param minLength độ dài tối thiểu của từ
     * @param maxLength độ dài tối đa của từ
     * @return WordChallenge chứa list chữ cái gốc và list chữ cái đã đảo
     */
    public static Optional<WordChallenge> generateWordChallenge(int minLength, int maxLength) {
        Optional<String> wordOpt = pickRandomWord(minLength, maxLength);
        if (!wordOpt.isPresent()) return Optional.empty();

        String originalWord = wordOpt.get();
        List<String> originalLetters = splitToCodepointStrings(originalWord);
        List<String> shuffledLetters = new ArrayList<>(originalLetters);

        // Đảo ngẫu nhiên các chữ cái
        Collections.shuffle(shuffledLetters, RAND);

        // Đảm bảo list đã đảo khác với list gốc
        int attempts = 0;
        while (shuffledLetters.equals(originalLetters) && attempts < 10) {
            Collections.shuffle(shuffledLetters, RAND);
            attempts++;
        }

        return Optional.of(new WordChallenge(originalWord, originalLetters, shuffledLetters));
    }

    /**
     * Kiểm tra xem list chữ cái người chơi chọn có đúng với từ gốc không
     * @param selectedLetters list chữ cái người chơi đã chọn theo thứ tự
     * @param originalWord từ gốc cần so sánh
     * @return true nếu đúng, false nếu sai
     */
    public static boolean checkAnswer(List<String> selectedLetters, String originalWord) {
        if (selectedLetters == null || originalWord == null) return false;
        String userAnswer = String.join("", selectedLetters);
        return normalize(userAnswer).equals(normalize(originalWord));
    }

    public static List<String> splitToCodepointStrings(String s) {
        if (s == null) return Collections.emptyList();
        return s.codePoints()
                .mapToObj(cp -> new String(Character.toChars(cp)))
                .collect(Collectors.toList());
    }

    public static String randomVietnameseLetter() {
        return VIET_LETTERS[RAND.nextInt(VIET_LETTERS.length)];
    }

    public static String normalize(String s) {
        if (s == null) return "";
        return s.trim().toLowerCase();
    }

    public static int codePointLength(String s) {
        return s == null ? 0 : s.codePointCount(0, s.length());
    }

    /**
     * Class chứa thông tin về một thử thách từ
     */
    public static class WordChallenge {
        private final String originalWord;
        private final List<String> originalLetters;
        private final List<String> shuffledLetters;

        public WordChallenge(String originalWord, List<String> originalLetters, List<String> shuffledLetters) {
            this.originalWord = originalWord;
            this.originalLetters = Collections.unmodifiableList(new ArrayList<>(originalLetters));
            this.shuffledLetters = Collections.unmodifiableList(new ArrayList<>(shuffledLetters));
        }

        public String getOriginalWord() {
            return originalWord;
        }

        public List<String> getOriginalLetters() {
            return originalLetters;
        }

        public List<String> getShuffledLetters() {
            return shuffledLetters;
        }

        @Override
        public String toString() {
            return "WordChallenge{" +
                    "originalWord='" + originalWord + '\'' +
                    ", originalLetters=" + originalLetters +
                    ", shuffledLetters=" + shuffledLetters +
                    '}';
        }
    }
}