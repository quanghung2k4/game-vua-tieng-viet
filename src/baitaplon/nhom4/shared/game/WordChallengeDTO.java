package baitaplon.nhom4.shared.game;

import java.io.Serializable;
import java.util.List;

public class WordChallengeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String originalWord;
    private List<String> originalLetters;
    private List<String> shuffledLetters;

    public WordChallengeDTO() {}

    public WordChallengeDTO(String originalWord, List<String> originalLetters, List<String> shuffledLetters) {
        this.originalWord = originalWord;
        this.originalLetters = originalLetters;
        this.shuffledLetters = shuffledLetters;
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
}
