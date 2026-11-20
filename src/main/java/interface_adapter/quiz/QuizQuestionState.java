package interface_adapter.quiz;

import java.util.List;

/** Display-ready question: the Swing view never sees domain entities. */
public record QuizQuestionState(String text, List<String> choices, int selectedChoice) {
    public QuizQuestionState {
        choices = List.copyOf(choices);
    }
}
