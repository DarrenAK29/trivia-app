package entity;

import java.util.ArrayList;

public class Quiz {
    final private String difficultyLevel;
    final private ArrayList<Question> questions;
    final private String category;
    final private String type;
    final private int questionCount;

    public Quiz(String difficultyLevel, ArrayList<Question> questions, String category, String type) {
        if (questions.isEmpty()) {
            throw new IllegalArgumentException(
                    "Quiz must contain at least one question"
            );
        }

        this.difficultyLevel = difficultyLevel;
        this.questions = new ArrayList<>(questions);
        this.category = category;
        this.type = type;
        this.questionCount = questions.size();
    }

    public ArrayList<Question> getQuestions() {
        return new ArrayList<>(this.questions);
    }

    public String getDifficultyLevel() {
        return this.difficultyLevel;
    }

    public String getCategory() {
        return this.category;
    }

    public String getType() {
        return this.type;
    }

    public int getQuestionCount() {
        return this.questionCount;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

}
