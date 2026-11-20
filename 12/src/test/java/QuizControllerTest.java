import interface_adapter.quiz.QuizController;
import use_case.quiz.QuizInputBoundary;
import use_case.quiz.QuizInputData;

/** Verifies the controller delegates every UI action through its input port. */
public final class QuizControllerTest {
    private static final class Capture implements QuizInputBoundary {
        String action;
        int choice;
        @Override public void startQuiz() { action = "start"; }
        @Override public void nextQuestion(QuizInputData input) {
            action = "next"; choice = input.selectedChoice();
        }
        @Override public void previousQuestion(QuizInputData input) {
            action = "previous"; choice = input.selectedChoice();
        }
        @Override public void submitQuiz(QuizInputData input) {
            action = "submit"; choice = input.selectedChoice();
        }
        @Override public void exitQuiz(QuizInputData input) {
            action = "exit"; choice = input.selectedChoice();
        }
    }

    public static void main(String[] args) {
        Capture capture = new Capture();
        QuizController controller = new QuizController(capture);
        controller.startQuiz();
        if (!capture.action.equals("start")) throw new AssertionError("start");
        controller.nextQuestion(2);
        if (!capture.action.equals("next") || capture.choice != 2) throw new AssertionError("next");
        controller.previousQuestion(1);
        if (!capture.action.equals("previous") || capture.choice != 1)
            throw new AssertionError("previous");
        controller.submitQuiz(-1);
        if (!capture.action.equals("submit") || capture.choice != -1)
            throw new AssertionError("submit");
        controller.exitQuiz(0);
        if (!capture.action.equals("exit") || capture.choice != 0) throw new AssertionError("exit");
        System.out.println("QuizControllerTest passed");
    }
}
