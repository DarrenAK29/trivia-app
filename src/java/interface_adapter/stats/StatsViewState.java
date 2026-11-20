package interface_adapter.stats;

import java.util.List;

public record StatsViewState(String summary, List<String> topScores, List<String> categoryAverages) {
    public StatsViewState {
        topScores = List.copyOf(topScores);
        categoryAverages = List.copyOf(categoryAverages);
    }

    public static StatsViewState empty() {
        return new StatsViewState("", List.of(), List.of());
    }
}
