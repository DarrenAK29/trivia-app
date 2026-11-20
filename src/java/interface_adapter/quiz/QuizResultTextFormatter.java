package interface_adapter.quiz;

import java.util.ArrayList;
import use_case.quiz.QuizSubmissionData;

/** Turns submission data into human-readable messages, outside the use case. */
public final class QuizResultTextFormatter {
    private QuizResultTextFormatter() { }

    public static String format(QuizSubmissionData submission) {
        if (submission.unansweredCount() > 0) {
            return "You have " + submission.unansweredCount() + " unanswered questions left!";
        }

        ArrayList<String> parts = new ArrayList<>();
        if (!submission.correctQuestionNumbers().isEmpty()) {
            parts.add("You correctly answered questions "
                    + String.join(", ", submission.correctQuestionNumbers()));
        }
        if (!submission.incorrectQuestionNumbers().isEmpty()) {
            parts.add("You incorrectly answered questions "
                    + String.join(", ", submission.incorrectQuestionNumbers()));
        }
        parts.add("Your score is " + submission.score() + "/" + submission.questionCount());
        parts.add("\n");
        return String.join(". ", parts);
    }
}
