package use_case.quiz;

import java.util.List;

/** UI-independent snapshot returned by any quiz operation. */
public record QuizOutputData(
        boolean inQuiz,
        int currentIndex,
        List<QuestionOutputData> questions,
        QuizSubmissionData submission,
        boolean exiting) {
    public QuizOutputData {
        questions = List.copyOf(questions);
    }
}
