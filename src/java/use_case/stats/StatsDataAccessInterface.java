package use_case.stats;

import entity.ScoreRecord;
import java.util.List;

public interface StatsDataAccessInterface {
    List<ScoreRecord> getAllScores();
}
