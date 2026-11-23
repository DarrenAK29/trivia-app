package use_case.quiz;

import entity.Question;
import entity.Quiz;
import entity.QuizConfiguration;
import entity.QuizSession;

import java.util.ArrayList;
import java.util.Objects;

public final class ConfigureQuizInteractor
        implements ConfigureQuizInputBoundary {

    private final QuizConfigFileDataAccessInterface quizConfigAccess;
    private final QuestionDataAccessInterface questionDataAccess;
    private final QuizSessionDataAccessInterface sessionDataAccess;
    private final ConfigureQuizOutputBoundary presenter;

    public ConfigureQuizInteractor(
            QuizConfigFileDataAccessInterface quizConfigAccess,
            QuestionDataAccessInterface questionDataAccess,
            QuizSessionDataAccessInterface sessionDataAccess,
            ConfigureQuizOutputBoundary presenter) {

        this.quizConfigAccess = quizConfigAccess;

        this.questionDataAccess =
                Objects.requireNonNull(questionDataAccess);

        this.sessionDataAccess =
                Objects.requireNonNull(sessionDataAccess);

        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void execute(QuizConfiguration config) {
        quizConfigAccess.saveConfig(config);
    }

    private String getDifficulty(QuizConfiguration config) {

        String difficulty = config.getDifficulty().getApiValue();

        if (difficulty.isEmpty()) {
            return "Any";
        }

        return difficulty;
    }

    private String getCategory(QuizConfiguration config) {

        Integer categoryId = config.getCategoryId();

        if (categoryId == null) {
            return "Any";
        }

        return String.valueOf(categoryId);
    }

    private String getType(QuizConfiguration config) {

        String type = config.getQuestionType().getApiValue();

        if (type.isEmpty()) {
            return "Any";
        }

        if (type.equals("multiple")) {
            return "Multiple Choice";
        }

        if (type.equals("boolean")) {
            return "True/False";
        }

        return type;
    }
}
