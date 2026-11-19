public class ReviewResultsPresenter
        implements ReviewResultsOutputBoundary {

    private final ReviewResultsViewModel viewModel;

    public ReviewResultsPresenter(
            ReviewResultsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ReviewResultsOutputData outputData) {
        String scoreText = "Score: " + outputData.getScore()
                + "/" + outputData.getTotal();

        StringBuilder reviewText = new StringBuilder();
        int number = 1;

        for (ReviewQuestionData question : outputData.getQuestions()) {
            reviewText.append("Question ").append(number).append(": ")
                    .append(question.getQuestion()).append("\n");
            reviewText.append("Your answer: ")
                    .append(question.getUserAnswer()).append("\n");
            reviewText.append("Correct answer: ")
                    .append(question.getCorrectAnswer()).append("\n");
            reviewText.append("Result: ")
                    .append(question.isCorrect() ? "Correct" : "Incorrect")
                    .append("\n\n");
            number++;
        }

        viewModel.setResult(scoreText, reviewText.toString());
    }
}