package use_case.stats;

public interface StatsOutputBoundary {
    void presentStats(StatsOutputData output);

    void presentNoStats(String message);
}
