import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ReviewResultsViewModel {
    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    private String scoreText = "";
    private String reviewText = "";

    public String getScoreText() {
        return scoreText;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setResult(String scoreText, String reviewText) {
        this.scoreText = scoreText;
        this.reviewText = reviewText;
        support.firePropertyChange("results", null, reviewText);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}