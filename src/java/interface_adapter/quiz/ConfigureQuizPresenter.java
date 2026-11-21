package interface_adapter.quiz;

import java.util.Objects;
import use_case.quiz.ConfigureQuizOutputBoundary;
import use_case.quiz.ConfigureQuizOutputData;

/**
 * Adapts application output to UI state.
 */
public final class ConfigureQuizPresenter implements ConfigureQuizOutputBoundary {

    private final ConfigureQuizViewModel viewModel;

    public ConfigureQuizPresenter(ConfigureQuizViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void prepareSuccessView(ConfigureQuizOutputData outputData) {
        viewModel.setState(
                ConfigureQuizViewState.success(outputData.getSession())
        );
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setState(
                ConfigureQuizViewState.failure(error)
        );
    }
}