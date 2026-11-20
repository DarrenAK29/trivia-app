package interface_adapter.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import use_case.stats.StatsOutputBoundary;
import use_case.stats.StatsOutputData;

public final class StatsPresenter implements StatsOutputBoundary {
    private final StatsViewModel viewModel;

    public StatsPresenter(StatsViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void presentStats(StatsOutputData output) {
        List<String> topScores = new ArrayList<>();
        int rank = 1;
        for (StatsOutputData.ScoreRow row : output.topScores()) {
            topScores.add(String.format("%d. %s (%s): %d/%d (%.0f%%)", rank, row.category(),
                    row.difficulty(), row.score(), row.total(), row.percent()));
            rank++;
        }

        List<String> categoryAverages = new ArrayList<>();
        for (Map.Entry<String, Double> entry : output.averageByCategory().entrySet()) {
            categoryAverages.add(String.format("%s: %.0f%%", entry.getKey(), entry.getValue()));
        }

        String summary = String.format("Quizzes played: %d   |   Average: %.0f%%",
                output.quizzesPlayed(), output.averagePercent());
        viewModel.setState(new StatsViewState(summary, topScores, categoryAverages));
    }

    @Override
    public void presentNoStats(String message) {
        viewModel.setState(new StatsViewState(message, List.of(), List.of()));
    }
}
