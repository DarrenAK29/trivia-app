package use_case.stats;

import entity.ScoreRecord;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StatsInteractor implements StatsInputBoundary {
    public static final int TOP_N = 10;

    private final StatsDataAccessInterface scores;
    private final StatsOutputBoundary presenter;

    public StatsInteractor(StatsDataAccessInterface scores, StatsOutputBoundary presenter) {
        this.scores = Objects.requireNonNull(scores);
        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void viewStats() {
        List<ScoreRecord> records = scores.getAllScores();
        if (records.isEmpty()) {
            presenter.presentNoStats("No quizzes played yet.");
            return;
        }

        List<StatsOutputData.ScoreRow> topScores = records.stream()
                .sorted(Comparator.comparingDouble(ScoreRecord::getPercent).reversed())
                .limit(TOP_N)
                .map(r -> new StatsOutputData.ScoreRow(r.getCategory(), r.getDifficulty(),
                        r.getScore(), r.getTotal(), r.getPercent()))
                .collect(Collectors.toList());

        double average = records.stream()
                .mapToDouble(ScoreRecord::getPercent)
                .average()
                .orElse(0);

        Map<String, Double> byCategory = records.stream()
                .collect(Collectors.groupingBy(ScoreRecord::getCategory,
                        Collectors.averagingDouble(ScoreRecord::getPercent)));

        presenter.presentStats(new StatsOutputData(topScores, records.size(), average, byCategory));
    }
}
