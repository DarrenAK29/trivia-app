package use_case.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Question data exposed to a presenter (never includes the correct answer). */
public record QuestionOutputData(String text, List<String> choices, int selectedChoice) {
    public QuestionOutputData {
        // The original Question allows null option labels. Keep that behavior while
        // making an immutable display snapshot (List.copyOf rejects nulls).
        choices = Collections.unmodifiableList(new ArrayList<>(choices));
    }
}
