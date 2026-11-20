package interface_adapter.quiz;

import java.util.Objects;
import use_case.quiz.QuizInputBoundary;
import use_case.quiz.QuizInputData;

/** Adapts button actions into application requests. */
public final class QuizController {
    private final QuizInputBoundary interactor;

    public QuizController(QuizInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    public void startQuiz() {
        interactor.startQuiz();
    }

    public void nextQuestion(int selectedChoice) {
        interactor.nextQuestion(new QuizInputData(selectedChoice));
    }

    public void previousQuestion(int selectedChoice) {
        interactor.previousQuestion(new QuizInputData(selectedChoice));
    }

    public void submitQuiz(int selectedChoice) {
        interactor.submitQuiz(new QuizInputData(selectedChoice));
    }

    public void exitQuiz(int selectedChoice) {
        interactor.exitQuiz(new QuizInputData(selectedChoice));
    }
}
