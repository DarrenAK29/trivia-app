package entity;

public class QuizConfiguration {

    public enum Difficulty {
        ANY(""),
        EASY("easy"),
        MEDIUM("medium"),
        HARD("hard");

        private final String apiValue;

        Difficulty(String apiValue) {
            this.apiValue = apiValue;
        }

        public String getApiValue() {
            return apiValue;
        }
    }

    public enum QuestionType {
        ANY(""),
        MULTIPLE_CHOICE("multiple"),
        TRUE_FALSE("boolean");

        private final String apiValue;

        QuestionType(String apiValue) {
            this.apiValue = apiValue;
        }

        public String getApiValue() {
            return apiValue;
        }
    }

    public enum Encoding {
        DEFAULT("default"),
        URL_SAFE("url3986"),
        BASE64("base64");

        private final String apiValue;

        Encoding(String apiValue) {
            this.apiValue = apiValue;
        }

        public String getApiValue() {
            return apiValue;
        }
    }

    private final int questionCount;
    private final Integer categoryId;
    private final Difficulty difficulty;
    private final QuestionType questionType;
    private final Encoding encoding;

    public QuizConfiguration(
            int questionCount,
            Integer categoryId,
            Difficulty difficulty,
            QuestionType questionType,
            Encoding encoding) {

        if (questionCount < 1 || questionCount > 50) {
            throw new IllegalArgumentException(
                    "Question count must be between 1 and 50.");
        }

        if (categoryId != null && categoryId <= 0) {
            throw new IllegalArgumentException(
                    "Category ID must be positive.");
        }

        this.questionCount = questionCount;
        this.categoryId = categoryId;
        this.difficulty = difficulty;
        this.questionType = questionType;
        this.encoding = encoding;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public Encoding getEncoding() {
        return encoding;
    }
}

