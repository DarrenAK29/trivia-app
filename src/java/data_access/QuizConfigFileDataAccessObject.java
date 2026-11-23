package data_access;

import entity.QuizConfiguration;
import entity.ScoreRecord;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import use_case.quiz.QuizConfigFileDataAccessInterface;
import use_case.quiz.ScoreSaveDataAccessInterface;
import use_case.stats.StatsDataAccessInterface;

//class TwoWayMap<K, V> {
//    private final Map<K, V> forward = new HashMap<>();
//    private final Map<V, K> backward = new HashMap<>();
//
//    public void put(K key, V value) {
//        // Remove old mappings if key or value already exists to prevent stale data
//        if (forward.containsKey(key)) {
//            backward.remove(forward.get(key));
//        }
//        if (backward.containsKey(value)) {
//            forward.remove(backward.get(value));
//        }
//        forward.put(key, value);
//        backward.put(value, key);
//    }
//
//    public V getByKey(K key) {
//        return forward.get(key);
//    }
//
//    public K getByValue(V value) {
//        return backward.get(value);
//    }
//}

public final class QuizConfigFileDataAccessObject implements QuizConfigFileDataAccessInterface {
    private static final String SEP = "\t";
    Map<String, Integer> numAndDifficulty = Map.of(
            "", 0,
            "easy", 1,
            "medium", 2,
            "hard", 3
    );

    Map<String, Integer> numAndType = Map.of(
            "", 0,
            "multiple", 1,
            "boolean", 2
    );

    Map<String, Integer> numAndEncoding = Map.of(
            "default", 0,
            "url3986", 1,
            "base64", 2
    );

    private final Path file;
    private QuizConfiguration config;

    private static Integer fileStringParser(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public QuizConfigFileDataAccessObject(String filePath) {
        this.file = Path.of(filePath);
    }

    @Override
    public void saveConfig(QuizConfiguration config) {
        String questionCount = String.valueOf(config.getQuestionCount());
        String category = String.valueOf(config.getCategoryId());
        String difficultyId = String.valueOf(numAndDifficulty.get(config.getDifficulty().getApiValue()));
        String typeId = String.valueOf(numAndType.get(config.getQuestionType().getApiValue()));
        String encodingId = String.valueOf(numAndEncoding.get(config.getEncoding().getApiValue()));

        String[] contents = {questionCount, category, difficultyId, typeId, encodingId};


        String line = String.join(SEP, contents);
        try {
            Files.writeString(file, line + System.lineSeparator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public QuizConfiguration loadConfig() {
        QuizConfiguration defaultConfig = new QuizConfiguration(
                10,
                null,
                QuizConfiguration.Difficulty.ANY,
                QuizConfiguration.QuestionType.ANY,
                QuizConfiguration.Encoding.DEFAULT
        );
        if (Files.exists(file)) {
            try {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                if (!lines.isEmpty() && !lines.get(0).isBlank() && !lines.get(0).isEmpty()) {
                    String line = lines.get(0);
                    String[] parts = line.split(SEP);
                    Integer questionCount = fileStringParser(parts[0]);
                    Integer categoryId = fileStringParser(parts[1]);
                    Integer difficultyValue = fileStringParser(parts[2]);
                    Integer typeValue = fileStringParser(parts[3]);
                    Integer encodingValue = fileStringParser(parts[4]);

                    return new QuizConfiguration(questionCount,
                            categoryId,
                            QuizConfiguration.Difficulty.values()[difficultyValue],
                            QuizConfiguration.QuestionType.values()[typeValue],
                            QuizConfiguration.Encoding.values()[encodingValue]
                    );
                } else {
                    return defaultConfig;
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return defaultConfig;
        }
    }
}
