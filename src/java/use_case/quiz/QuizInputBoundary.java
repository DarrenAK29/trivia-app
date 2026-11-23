package use_case.quiz;

import entity.QuizConfiguration;

/** Operations the UI may request; the UI knows nothing about their implementation. */
public interface QuizInputBoundary {
    void startNewQuiz(QuizConfiguration qc);
    void startQuiz();
    void nextQuestion(QuizInputData input);
    void previousQuestion(QuizInputData input);
    void submitQuiz(QuizInputData input);
    void exitQuiz(QuizInputData input);
}
