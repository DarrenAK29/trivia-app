import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.QuizViewModel;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import use_case.quiz.QuestionOutputData;
import use_case.quiz.QuizOutputData;
import use_case.quiz.QuizSubmissionData;

/** Headless presentation checks, including the one-operation message format. */
public final class QuizPresenterTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        QuizViewModel model = new QuizViewModel();
        QuizPresenter presenter = new QuizPresenter(model);
        AtomicInteger events = new AtomicInteger();
        model.addPropertyChangeListener(event -> events.incrementAndGet());
        List<QuestionOutputData> questions = List.of(
                new QuestionOutputData("One?", List.of("Yes", "No"), 0),
                new QuestionOutputData("Two?", List.of("True", "False"), -1));
        presenter.present(new QuizOutputData(true, 1, questions, null, false));
        check(model.getState().inQuiz(), "quiz state is displayed");
        check(model.getState().currentIndex() == 1, "current index mapped");
        check(model.getState().questions().get(0).selectedChoice() == 0, "selected choice mapped");
        check(model.getState().message() == null, "navigation has no message");

        QuizSubmissionData incomplete = new QuizSubmissionData(1, 0, 2, List.of(), List.of());
        presenter.present(new QuizOutputData(true, 1, questions, incomplete, false));
        check("You have 1 unanswered questions left!".equals(model.getState().message()),
                "incomplete submission message preserved");
        QuizSubmissionData complete = new QuizSubmissionData(0, 1, 2, List.of("1"), List.of("2"));
        presenter.present(new QuizOutputData(true, 1, questions, complete, false));
        check(model.getState().message().contains("You correctly answered questions 1"),
                "correct answers formatted");
        check(model.getState().message().contains("Your score is 1/2"),
                "score formatted");
        presenter.present(new QuizOutputData(false, 1, questions, null, true));
        check("Selected choices are [0, -1]. Currently on index 1".equals(
                model.getState().message()), "exit summary preserved");
        presenter.present(new QuizOutputData(false, 1, questions, null, true));
        check(events.get() == 5, "repeat identical events still notify the view");
        System.out.println("QuizPresenterTest passed");
    }
}
