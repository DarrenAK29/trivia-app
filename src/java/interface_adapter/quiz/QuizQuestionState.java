package interface_adapter.quiz;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Collections;
=======
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
import java.util.List;

/** Display-ready question: the Swing view never sees domain entities. */
public record QuizQuestionState(String text, List<String> choices, int selectedChoice) {
    public QuizQuestionState {
<<<<<<< HEAD
        // The original Question allows null option labels. Keep that behavior while
        // making an immutable display snapshot (List.copyOf rejects nulls).
        choices = Collections.unmodifiableList(new ArrayList<>(choices));
=======
        choices = List.copyOf(choices);
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
    }
}
