package data_access;

import entity.QuizSession;
import java.util.Objects;
import use_case.quiz.QuizSessionDataAccessInterface;

/** In-memory storage for the demo: no database is required. */
public final class InMemoryQuizSessionDataAccessObject implements QuizSessionDataAccessInterface {
    private QuizSession session;

    public InMemoryQuizSessionDataAccessObject(QuizSession initialSession) {
        this.session = Objects.requireNonNull(initialSession);
    }

    @Override
    public QuizSession loadSession() {
        return session;
    }

    @Override
    public void saveSession(QuizSession session) {
        this.session = Objects.requireNonNull(session);
    }
}
