package baitaplon.nhom4.server;

import baitaplon.nhom4.shared.game.WordBatchDTO;
import baitaplon.nhom4.shared.game.WordChallengeDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameWordService {

    private static volatile boolean loaded = false;

    public static synchronized void init(String dictionaryPath) throws IOException {
        if (!loaded) {
            WordDictionary.loadFromFile(dictionaryPath);
            loaded = true;
        }
    }

    public static WordBatchDTO generateBatch(int size, int minLen, int maxLen) {
        List<WordChallengeDTO> list = new ArrayList<>();
        int attempts = 0;
        while (list.size() < size && attempts < size * 10) {
            attempts++;
            WordDictionary.generateWordChallenge(minLen, maxLen).ifPresent(ch -> {
                list.add(new WordChallengeDTO(ch.getOriginalWord(), ch.getOriginalLetters(), ch.getShuffledLetters()));
            });
        }
        System.out.println("Generated " + list.size() + " words in " + attempts + " attempts");
        for(WordChallengeDTO dto : list) {
            System.out.println(dto.getOriginalWord() + " " + dto.getOriginalLetters());
        }
        return new WordBatchDTO(list);
    }
}
