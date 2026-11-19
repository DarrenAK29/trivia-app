public class ReviewQuestionData {
    private final String question;
    private final String userAnswer;
    private final String correctAnswer;
    private final boolean correct;

    public ReviewQuestionData(String question, String userAnswer,
                              String correctAnswer, boolean correct) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}
