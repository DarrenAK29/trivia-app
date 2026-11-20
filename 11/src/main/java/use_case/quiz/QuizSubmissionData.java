package use_case.quiz;

import java.util.List;

/** A submission attempt: incomplete when unansweredCount > 0. */
public record QuizSubmissionData(
        int unansweredCount,
        int score,
        int questionCount,
        List<String> correctQuestionNumbers,
        List<String> incorrectQuestionNumbers) {
    public QuizSubmissionData {
        correctQuestionNumbers = List.copyOf(correctQuestionNumbers);
        incorrectQuestionNumbers = List.copyOf(incorrectQuestionNumbers);
    }
}
