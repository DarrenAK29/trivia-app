package use_case.quiz;

public interface ConfigureQuizOutputBoundary {

    void prepareSuccessView(ConfigureQuizOutputData outputData);

    void prepareFailView(String error);
}
