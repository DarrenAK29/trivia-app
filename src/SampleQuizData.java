import java.util.ArrayList;
import java.util.Arrays;

/** Demo quiz and prefilled session, kept separate from the Swing view. */
public final class SampleQuizData {
    private SampleQuizData() { }

    public static QuizSession createSession() {
        String[] questions = {
                "How hard is Scarbo?",
                "Who wrote Scarbo?",
                "How long is Scarbo in minutes?",
                "Can Muyuan play Scarbo?"
        };
        ArrayList<ArrayList<String>> choices = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("Piss easy", "Hard as fuck", "Moderately difficult")),
                new ArrayList<>(Arrays.asList("Ravel", "Beethoven", "Bach")),
                new ArrayList<>(Arrays.asList("7", "8", "9")),
                new ArrayList<>(Arrays.asList("Yes", "No"))
        ));
        int[] correct = {1, 0, 2, 0};
        ArrayList<Question> questionList = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            questionList.add(new Question(questions[i], choices.get(i), correct[i]));
        }
        Quiz quiz = new Quiz("Hard", questionList, "Scarbo", "MCQ");

        // Preserve the original prototype's pre-selected answers and starting page.
        int[] selected = {1, 0, 2, 0};
        return new QuizSession(quiz, selected, 2, 0);
    }
}
