package use_case.quiz;

import entity.QuizSession;

public class ConfigureQuizOutputData {

    private final QuizSession session;

    public ConfigureQuizOutputData(QuizSession session) {
        this.session = session;
    }

    public QuizSession getSession() {
        return session;
    }
}
