package data_access;

import entity.ScoreRecord;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import use_case.quiz.ScoreSaveDataAccessInterface;
import use_case.stats.StatsDataAccessInterface;

public final class FileScoreDataAccessObject implements ScoreSaveDataAccessInterface, StatsDataAccessInterface {
    private static final String SEP = "\t";

    private final Path file;
    private final List<ScoreRecord> records = new ArrayList<>();

    public FileScoreDataAccessObject(String filePath) {
        this.file = Path.of(filePath);
        if (Files.exists(file)) {
            try {
                for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                    if (!line.isBlank()) {
                        String[] parts = line.split(SEP);
                        records.add(new ScoreRecord(parts[0], parts[1],
                                Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public void saveScore(ScoreRecord record) {
        records.add(record);
        String line = String.join(SEP, clean(record.getCategory()), clean(record.getDifficulty()),
                String.valueOf(record.getScore()), String.valueOf(record.getTotal()));
        try {
            Files.writeString(file, line + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<ScoreRecord> getAllScores() {
        return new ArrayList<>(records);
    }

    private static String clean(String text) {
        return text.replace(SEP, " ").replace("\n", " ").replace("\r", " ");
    }
}
