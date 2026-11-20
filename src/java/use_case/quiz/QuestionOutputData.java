package use_case.quiz;

import java.util.List;

/** Question data exposed to a presenter (never includes the correct answer). */
public record QuestionOutputData(String text, List<String> choices, int selectedChoice) {
    public QuestionOutputData {
        choices = List.copyOf(choices);
    }
}
