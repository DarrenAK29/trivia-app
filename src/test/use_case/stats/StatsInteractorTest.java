package use_case.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import entity.ScoreRecord;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatsInteractorTest {

    private static StatsDataAccessInterface scoresOf(ScoreRecord... records) {
        List<ScoreRecord> list = new ArrayList<>(List.of(records));
        return () -> new ArrayList<>(list);
    }

    private static class FakePresenter implements StatsOutputBoundary {
        StatsOutputData output;
        String message;

        @Override
        public void presentStats(StatsOutputData output) {
            this.output = output;
        }

        @Override
        public void presentNoStats(String message) {
            this.message = message;
        }
    }

    @Test
    void noScoresPresentsMessage() {
        FakePresenter presenter = new FakePresenter();
        new StatsInteractor(scoresOf(), presenter).viewStats();

        assertEquals("No quizzes played yet.", presenter.message);
        assertNull(presenter.output);
    }

    @Test
    void computesTotalsAndAverages() {
        FakePresenter presenter = new FakePresenter();
        new StatsInteractor(scoresOf(
                new ScoreRecord("Music", "Hard", 4, 4),
                new ScoreRecord("Music", "Hard", 2, 4),
                new ScoreRecord("History", "Easy", 0, 2)),
                presenter).viewStats();

        assertEquals(3, presenter.output.quizzesPlayed());
        assertEquals(50.0, presenter.output.averagePercent(), 0.001);
        assertEquals(75.0, presenter.output.averageByCategory().get("Music"), 0.001);
        assertEquals(0.0, presenter.output.averageByCategory().get("History"), 0.001);
    }

    @Test
    void topScoresAreHighestFirst() {
        FakePresenter presenter = new FakePresenter();
        new StatsInteractor(scoresOf(
                new ScoreRecord("A", "Easy", 1, 4),
                new ScoreRecord("B", "Easy", 4, 4),
                new ScoreRecord("C", "Easy", 2, 4)),
                presenter).viewStats();

        List<StatsOutputData.ScoreRow> top = presenter.output.topScores();
        assertEquals("B", top.get(0).category());
        assertEquals("C", top.get(1).category());
        assertEquals("A", top.get(2).category());
    }

    @Test
    void topScoresAreCappedAtTen() {
        ScoreRecord[] records = new ScoreRecord[15];
        for (int i = 0; i < records.length; i++) {
            records[i] = new ScoreRecord("Music", "Easy", i % 5, 4);
        }
        FakePresenter presenter = new FakePresenter();
        new StatsInteractor(scoresOf(records), presenter).viewStats();

        assertEquals(StatsInteractor.TOP_N, presenter.output.topScores().size());
        assertEquals(15, presenter.output.quizzesPlayed());
    }
}
