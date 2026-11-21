package use_case.quiz;

import entity.Question;
import entity.Quiz;
import entity.QuizConfiguration;
import entity.QuizSession;

import java.util.ArrayList;
import java.util.Objects;

public final class ConfigureQuizInteractor
        implements ConfigureQuizInputBoundary {

    private final QuestionDataAccessInterface questionDataAccess;
    private final QuizSessionDataAccessInterface sessionDataAccess;
    private final ConfigureQuizOutputBoundary presenter;

    public ConfigureQuizInteractor(
            QuestionDataAccessInterface questionDataAccess,
            QuizSessionDataAccessInterface sessionDataAccess,
            ConfigureQuizOutputBoundary presenter) {

        this.questionDataAccess =
                Objects.requireNonNull(questionDataAccess);

        this.sessionDataAccess =
                Objects.requireNonNull(sessionDataAccess);

        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void execute(QuizConfiguration config) {

        Objects.requireNonNull(config, "Quiz configuration cannot be null.");

        ConfigureQuizOutputData outputData;

        try {
            // 1. Fetch questions using the user's configuration.
            ArrayList<Question> questions =
                    questionDataAccess.fetchQuestions(config);

            // 2. Check that the API returned questions.
            if (questions == null || questions.isEmpty()) {
                throw new IllegalStateException(
                        "No questions were returned. Please try another configuration."
                );
            }

            // 3. Convert the configuration values into quiz metadata.
            String difficulty = getDifficulty(config);
            String category = getCategory(config);
            String type = getType(config);

            // 4. Create the Quiz entity.
            Quiz quiz = new Quiz(
                    difficulty,
                    questions,
                    category,
                    type
            );

            // 5. Create a new QuizSession with no answers selected.
            // Session ID is 0 because the current DAO stores one session.
            QuizSession session = new QuizSession(quiz, 0);

            // 6. Save the generated session.
            sessionDataAccess.saveSession(session);

            // 7. Prepare the output for the presenter.
            outputData = new ConfigureQuizOutputData(session);

        } catch (RuntimeException e) {

            String errorMessage = e.getMessage();

            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = "An unexpected error occurred while creating the quiz.";
            }

            presenter.prepareFailView(errorMessage);
            return;
        }

        // 8. Notify the presenter that the quiz was created successfully.
        presenter.prepareSuccessView(outputData);
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
