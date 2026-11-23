package use_case.quiz;

import entity.QuizConfiguration;

public interface QuizConfigFileDataAccessInterface {
    void saveConfig(QuizConfiguration config);
    QuizConfiguration loadConfig();
}
