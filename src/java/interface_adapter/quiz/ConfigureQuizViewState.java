package interface_adapter.quiz;

import entity.QuizSession;

/**
 * Stores the state of the configure quiz screen.
 */
public final class ConfigureQuizViewState {

    private final QuizSession session;
    private final String error;
    private final boolean successful;

    private ConfigureQuizViewState(
            QuizSession session,
            String error,
            boolean successful
    ) {
        this.session = session;
        this.error = error;
        this.successful = successful;
    }

    public static ConfigureQuizViewState initial() {
        return new ConfigureQuizViewState(null, null, false);
    }

    public static ConfigureQuizViewState success(QuizSession session) {
        return new ConfigureQuizViewState(session, null, true);
    }

    public static ConfigureQuizViewState failure(String error) {
        return new ConfigureQuizViewState(null, error, false);
    }

    public QuizSession getSession() {
        return session;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccessful() {
        return successful;
    }
}