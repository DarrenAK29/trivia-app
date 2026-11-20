package use_case.quiz;

/** Application-to-presentation port. */
public interface QuizOutputBoundary {
    void present(QuizOutputData output);
}
