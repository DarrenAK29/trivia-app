package use_case.quiz;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Collections;
=======
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
import java.util.List;

/** Question data exposed to a presenter (never includes the correct answer). */
public record QuestionOutputData(String text, List<String> choices, int selectedChoice) {
    public QuestionOutputData {
<<<<<<< HEAD
        // The original Question allows null option labels. Keep that behavior while
        // making an immutable display snapshot (List.copyOf rejects nulls).
        choices = Collections.unmodifiableList(new ArrayList<>(choices));
=======
        choices = List.copyOf(choices);
>>>>>>> c475963558e459cf19ef9d70a743e92ba2dabbe7
    }
}
