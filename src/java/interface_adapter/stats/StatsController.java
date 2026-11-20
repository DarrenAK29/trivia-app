package interface_adapter.stats;

import java.util.Objects;
import use_case.stats.StatsInputBoundary;

public final class StatsController {
    private final StatsInputBoundary interactor;

    public StatsController(StatsInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void viewStats() {
        interactor.viewStats();
    }
}
