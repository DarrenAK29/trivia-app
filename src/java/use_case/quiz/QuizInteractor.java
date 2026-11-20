package use_case.quiz;

import entity.Question;
import entity.QuizResult;
import entity.QuizSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Coordinates the quiz; contains no Swing, printing, or database code. */
public final class QuizInteractor implements QuizInputBoundary {
    private final QuizSessionDataAccessInterface sessions;
    private final QuizOutputBoundary presenter;

    public QuizInteractor(QuizSessionDataAccessInterface sessions, QuizOutputBoundary presenter) {
        this.sessions = Objects.requireNonNull(sessions);
        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void startQuiz() {
        present(sessions.loadSession(), true, null, false);
    }

    @Override
    public void nextQuestion(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        session.nextQuestion();
        sessions.saveSession(session);
        present(session, true, null, false);
    }

    @Override
    public void previousQuestion(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        session.prevQuestion();
        sessions.saveSession(session);
        present(session, true, null, false);
    }

    @Override
    public void submitQuiz(QuizInputData input) {
        QuizSession session = saveAnswer(input);
<<<<<<< HEAD
        sessions.saveSession(session);
=======
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
        QuizResult result = session.submissionResult();
        int unanswered = result.getUnanswered().size();
        QuizSubmissionData submission;
        if (unanswered > 0) {
            // Incomplete submissions do not disclose intermediate scores.
            submission = new QuizSubmissionData(unanswered, 0, result.getSize(), List.of(), List.of());
        } else {
            submission = new QuizSubmissionData(0, result.getScore(), result.getSize(),
                    result.getCorrect(), result.getIncorrect());
        }
        present(session, true, submission, false);
    }

    @Override
    public void exitQuiz(QuizInputData input) {
        QuizSession session = saveAnswer(input);
<<<<<<< HEAD
        sessions.saveSession(session);
=======
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
        present(session, false, null, true);
    }

    private QuizSession saveAnswer(QuizInputData input) {
        Objects.requireNonNull(input, "input");
        QuizSession session = sessions.loadSession();
        session.editSelectedChoices(session.getCurIndex(), input.selectedChoice());
<<<<<<< HEAD
=======
        sessions.saveSession(session);
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
        return session;
    }

    private void present(QuizSession session, boolean inQuiz,
                         QuizSubmissionData submission, boolean exiting) {
        List<QuestionOutputData> questions = new ArrayList<>();
        for (int i = 0; i < session.getQuiz().getQuestionCount(); i++) {
            Question question = session.getQuiz().getQuestion(i);
            questions.add(new QuestionOutputData(question.getQuestionContent(),
                    question.getChoices(), session.getSelectedChoice(i)));
        }
        presenter.present(new QuizOutputData(inQuiz, session.getCurIndex(),
                questions, submission, exiting));
    }
}
