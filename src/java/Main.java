import data_access.InMemoryQuizSessionDataAccessObject;
import data_access.SampleQuizData;
<<<<<<< HEAD
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.QuizViewModel;
import use_case.quiz.QuizInputBoundary;
import use_case.quiz.QuizInteractor;
import use_case.quiz.QuizOutputBoundary;
import use_case.quiz.QuizSessionDataAccessInterface;
import view.QuizView;

/** Composition root: the only place where all architectural layers are wired. */
public final class Main {
    public static void main(String[] args) {
        QuizSessionDataAccessInterface sessions = new InMemoryQuizSessionDataAccessObject(
                SampleQuizData.createSession());
        QuizViewModel viewModel = new QuizViewModel();
        QuizOutputBoundary presenter = new QuizPresenter(viewModel);
        QuizInputBoundary interactor = new QuizInteractor(sessions, presenter);
        QuizController controller = new QuizController(interactor);
        new QuizView(controller, viewModel);
=======

public class Main {
    public static void main(String[] args) {
        new QuizView(new InMemoryQuizSessionDataAccessObject(SampleQuizData.createSession()));
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
    }
}
