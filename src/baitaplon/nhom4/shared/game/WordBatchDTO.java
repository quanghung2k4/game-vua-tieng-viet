package baitaplon.nhom4.shared.game;

import java.io.Serializable;
import java.util.List;

public class WordBatchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<WordChallengeDTO> challenges;

    public WordBatchDTO() {}

    public WordBatchDTO(List<WordChallengeDTO> challenges) {
        this.challenges = challenges;
    }

    public List<WordChallengeDTO> getChallenges() {
        return challenges;
    }
}