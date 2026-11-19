import data_access.InMemoryQuizSessionDataAccessObject;
import entity.QuizSession;
import use_case.quiz.QuizInputData;
import use_case.quiz.QuizInteractor;
import use_case.quiz.QuizOutputBoundary;
import use_case.quiz.QuizOutputData;
import use_case.quiz.QuizSubmissionData;

/** No external test dependency; run with scripts/test.sh. */
public final class QuizInteractorTest {
    private static final class Capture implements QuizOutputBoundary {
        QuizOutputData last;
        int presentations;
        @Override
        public void present(QuizOutputData output) {
            last = output;
            presentations++;
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        QuizSession seeded = SampleQuizData.createSession();
        Capture seededOutput = new Capture();
        QuizInteractor seededUseCase = new QuizInteractor(
                new InMemoryQuizSessionDataAccessObject(seeded), seededOutput);
        seededUseCase.startQuiz();
        check(seededOutput.last.inQuiz(), "start shows the quiz");
        check(seededOutput.last.currentIndex() == 2, "preserve seeded index");
        check(seededOutput.last.questions().get(3).selectedChoice() == 0,
                "preserve seeded selections");
        check(seededOutput.last.questions().size() == 4, "all questions are available");

        // A new, unanswered session tests the entire sequence independently of Swing.
        QuizSession blank = new QuizSession(seeded.getQuiz(), 123);
        InMemoryQuizSessionDataAccessObject repository =
                new InMemoryQuizSessionDataAccessObject(blank);
        Capture output = new Capture();
        QuizInteractor quiz = new QuizInteractor(repository, output);

        quiz.startQuiz();
        check(output.last.currentIndex() == 0, "new session starts at first question");
        quiz.previousQuestion(new QuizInputData(-1));
        check(output.last.currentIndex() == 0, "previous is clamped at first question");
        quiz.submitQuiz(new QuizInputData(-1));
        QuizSubmissionData submission = output.last.submission();
        check(submission.unansweredCount() == 4, "unanswered questions are counted");
        check(submission.correctQuestionNumbers().isEmpty(), "do not reveal partial marks");

        quiz.nextQuestion(new QuizInputData(1));
        check(repository.loadSession().getSelectedChoice(0) == 1, "save answer before next");
        quiz.nextQuestion(new QuizInputData(0));
        quiz.nextQuestion(new QuizInputData(2));
        quiz.nextQuestion(new QuizInputData(1));
        check(output.last.currentIndex() == 3, "next is clamped at last question");
        quiz.submitQuiz(new QuizInputData(1));
        submission = output.last.submission();
        check(submission.unansweredCount() == 0, "complete submission");
        check(submission.score() == 3 && submission.questionCount() == 4,
                "domain grading: 3 out of 4");
        check(submission.correctQuestionNumbers().size() == 3
                        && submission.incorrectQuestionNumbers().contains("4"),
                "correct/incorrect question numbers");

        quiz.exitQuiz(new QuizInputData(0));
        check(!output.last.inQuiz() && output.last.exiting(), "exit returns to home");
        check(repository.loadSession().getSelectedChoice(3) == 0, "save current answer on exit");
        quiz.startQuiz();
        check(output.last.currentIndex() == 3 && output.last.questions().get(3).selectedChoice() == 0,
                "resuming a session preserves state");
        quiz.submitQuiz(new QuizInputData(0));
        check(output.last.submission().score() == 4, "resubmission reflects saved answer");
        check(output.presentations == 11, "every command presents one state");
        System.out.println("QuizInteractorTest passed");
    }
}
