import java.util.ArrayList;

class Question {
    final private String questionContent;
    final private ArrayList<String> choices;
    final private int correctChoice;

    public Question(String questionContent, ArrayList<String> choices, int correctChoice) {
        if (choices.isEmpty()) {
            throw new IllegalArgumentException(
                    "Question must have at least one choice"
            );
        }

        if (correctChoice < 0 || correctChoice >= choices.size()) {
            throw new IllegalArgumentException(
                    "Correct choice index is out of bounds"
            );
        }

        this.questionContent = questionContent;
        this.choices = new ArrayList<>(choices);
        this.correctChoice = correctChoice;
    }

    public String getQuestionContent() {
        return this.questionContent;
    }

    public ArrayList<String> getChoices() {
        return new ArrayList<>(this.choices);
    }

    public int getCorrectChoice() {
        return this.correctChoice;
    }

    public int getChoiceCount() {
        return choices.size();
    }

}
