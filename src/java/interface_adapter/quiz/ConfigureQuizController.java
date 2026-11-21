package interface_adapter.quiz;

import java.util.Objects;
import entity.QuizConfiguration;
import use_case.quiz.ConfigureQuizInputBoundary;

/**
 * Adapts configuration form actions into application requests.
 */
public final class ConfigureQuizController {
    private final ConfigureQuizInputBoundary interactor;

    public ConfigureQuizController(ConfigureQuizInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void configureQuiz(QuizConfiguration config) {
        interactor.execute(config);
    }
}