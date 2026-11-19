import java.util.ArrayList;
import java.util.List;

public class ReviewResultsInteractor
        implements ReviewResultsInputBoundary {

    private final ReviewResultsOutputBoundary presenter;

    public ReviewResultsInteractor(
            ReviewResultsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(QuizResult result) {
        List<Question> questions = result.getQuestions();
        List<String> selectedChoices = result.getCorrectChoices();
        List<ReviewQuestionData> reviews = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            List<String> choices = question.getChoices();

            int selectedIndex =
                    Integer.parseInt(selectedChoices.get(i));
            int correctIndex = question.getCorrectChoice();

            String userAnswer;
            if (selectedIndex == -1) {
                userAnswer = "Not answered";
            } else {
                userAnswer = choices.get(selectedIndex);
            }

            String correctAnswer = choices.get(correctIndex);
            boolean correct = selectedIndex == correctIndex;

            reviews.add(new ReviewQuestionData(
                    question.getQuestionContent(),
                    userAnswer,
                    correctAnswer,
                    correct
            ));
        }

        ReviewResultsOutputData outputData =
                new ReviewResultsOutputData(
                        result.getScore(),
                        result.getSize(),
                        reviews
                );

        presenter.present(outputData);
    }
}