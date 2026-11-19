public class ReviewResultsController {
    private final ReviewResultsInputBoundary interactor;

    public ReviewResultsController(
            ReviewResultsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(QuizResult result) {
        interactor.execute(result);
    }
}