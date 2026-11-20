package interface_adapter.quiz;

import java.util.List;

/** Immutable state consumed by the UI, including a one-operation message. */
public record QuizViewState(
        boolean inQuiz,
        int currentIndex,
        List<QuizQuestionState> questions,
        String message) {
    public QuizViewState {
        questions = List.copyOf(questions);
    }

    public static QuizViewState home() {
        return new QuizViewState(false, 0, List.of(), null);
    }
}
