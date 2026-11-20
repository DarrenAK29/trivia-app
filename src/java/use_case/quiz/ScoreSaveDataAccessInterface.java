package use_case.quiz;

import entity.ScoreRecord;

public interface ScoreSaveDataAccessInterface {
    void saveScore(ScoreRecord record);
}
