package use_case.stats;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record StatsOutputData(
        List<ScoreRow> topScores,
        int quizzesPlayed,
        double averagePercent,
        Map<String, Double> averageByCategory) {

    public record ScoreRow(String category, String difficulty, int score, int total, double percent) { }

    public StatsOutputData {
        topScores = List.copyOf(topScores);
        averageByCategory = Collections.unmodifiableMap(new TreeMap<>(averageByCategory));
    }
}
