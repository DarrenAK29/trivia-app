package entity;

public class ScoreRecord {
    private final String category;
    private final String difficulty;
    private final int score;
    private final int total;

    public ScoreRecord(String category, String difficulty, int score, int total) {
        if (total <= 0) {
            throw new IllegalArgumentException("Total must be positive");
        }
        if (score < 0 || score > total) {
            throw new IllegalArgumentException("Score must be between 0 and total");
        }
        this.category = category;
        this.difficulty = difficulty;
        this.score = score;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public double getPercent() {
        return 100.0 * score / total;
    }
}
