package interface_adapter.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Display-ready question: the Swing view never sees domain entities. */
public record QuizQuestionState(String text, List<String> choices, int selectedChoice) {
    public QuizQuestionState {
        // The original Question allows null option labels. Keep that behavior while
        // making an immutable display snapshot (List.copyOf rejects nulls).
        choices = Collections.unmodifiableList(new ArrayList<>(choices));
    }
}
