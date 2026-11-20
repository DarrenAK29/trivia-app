package interface_adapter.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import use_case.quiz.QuestionOutputData;
import use_case.quiz.QuizOutputBoundary;
import use_case.quiz.QuizOutputData;

/** Adapts application output to UI state, including its text formatting. */
public final class QuizPresenter implements QuizOutputBoundary {
    private final QuizViewModel viewModel;

    public QuizPresenter(QuizViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void present(QuizOutputData output) {
        List<QuizQuestionState> questions = new ArrayList<>();
        for (QuestionOutputData question : output.questions()) {
            questions.add(new QuizQuestionState(question.text(),
                    question.choices(), question.selectedChoice()));
        }

        String message = null;
        if (output.submission() != null) {
            message = QuizResultTextFormatter.format(output.submission());
        } else if (output.exiting()) {
            int[] selections = questions.stream()
                    .mapToInt(QuizQuestionState::selectedChoice).toArray();
            message = "Selected choices are " + Arrays.toString(selections)
                    + ". Currently on index " + output.currentIndex();
        }

        viewModel.setState(new QuizViewState(output.inQuiz(),
                output.currentIndex(), questions, message));
    }
}
