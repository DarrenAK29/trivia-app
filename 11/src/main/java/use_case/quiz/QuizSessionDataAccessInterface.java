package use_case.quiz;

import entity.QuizSession;

/** Persistence port; use cases never need to know which storage system is used. */
public interface QuizSessionDataAccessInterface {
    QuizSession loadSession();
    void saveSession(QuizSession session);
}
