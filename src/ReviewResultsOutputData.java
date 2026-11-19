import java.util.ArrayList;
import java.util.List;

public class ReviewResultsOutputData {
    private final int score;
    private final int total;
    private final List<ReviewQuestionData> questions;

    public ReviewResultsOutputData(int score, int total,
                                   List<ReviewQuestionData> questions) {
        this.score = score;
        this.total = total;
        this.questions = new ArrayList<>(questions);
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public List<ReviewQuestionData> getQuestions() {
        return new ArrayList<>(questions);
    }
}