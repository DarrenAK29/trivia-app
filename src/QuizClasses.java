import java.util.ArrayList;
import java.util.*;

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

class Quiz {
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

class QuizSession {
    private Quiz quiz;
    private int curIndex;
    private int[] selectedChoices;

    public QuizSession(Quiz quiz, int[] selectedChoices, int curIndex) {
        if (selectedChoices.length != quiz.getQuestionCount()) {
            throw new IllegalArgumentException(
                    "Answer count must match question count"
            );
        }

        if (curIndex < 0 || curIndex >= quiz.getQuestionCount()) {
            throw new IllegalArgumentException(
                    "Invalid current question index"
            );
        }

        for (int i = 0; i < selectedChoices.length; i++) {
            int choice = selectedChoices[i];
            int choiceCount = quiz.getQuestion(i).getChoiceCount();

            if (choice < -1 || choice >= choiceCount) {
                throw new IllegalArgumentException(
                        "Invalid selected choice for question " + i
                );
            }
        }

        this.quiz = quiz;
        this.selectedChoices = selectedChoices.clone();
        this.curIndex = curIndex;


    }

    public QuizSession(Quiz quiz) {
        this(quiz, createEmptyChoices(quiz.getQuestionCount()), 0);
    }

    public QuizSession(Quiz quiz, int[] selectedChoices) {
        this(quiz, selectedChoices, 0);
    }

    public QuizSession(Quiz quiz, int curIndex) {
        this(quiz, createEmptyChoices(quiz.getQuestionCount()), curIndex);
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public int getCurIndex() {
        return this.curIndex;
    }

    public void nextQuestion() {
        if (curIndex < quiz.getQuestionCount() - 1) {
            curIndex++;
        }
    }

    public void prevQuestion() {
        if (curIndex > 0) {
            this.curIndex--;
        }
    }

    public QuizResult submissionResult() {
        return new QuizResult(quiz.getQuestions(), selectedChoices);
    }

    private static int[] createEmptyChoices(int size) {
        int[] empty = new int[size];
        Arrays.fill(empty, -1);
        return empty;
    }

    public int getSelectedChoice(int index) {
        return selectedChoices[index];
    }

    public void editSelectedChoices(int questionInd, int choice) {
        int choiceCount = quiz.getQuestion(questionInd).getChoiceCount();

        if (choice < -1 || choice >= choiceCount) {
            throw new IllegalArgumentException(
                    "Invalid answer for question " + questionInd
            );
        }

        selectedChoices[questionInd] = choice;
    }
}

class QuizResult {
    private int score;
    private int size;
    private ArrayList<String> correct = new ArrayList<>();
    private ArrayList<String> incorrect = new ArrayList<>();
    private ArrayList<String> unanswered = new ArrayList<>();

    public QuizResult(ArrayList<Question> questions, int[] selectedChoices) {
        for (int i = 0; i < questions.size(); i++) {
            size++;
            int choice = selectedChoices[i];
            if (choice == -1) {
                unanswered.add(String.valueOf(i + 1));
            } else if (choice == questions.get(i).getCorrectChoice()) {
                correct.add(String.valueOf(i + 1));
                score++;
            } else {
                incorrect.add(String.valueOf(i + 1));
            }
        }
    }

    public int getScore() {
        return score;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<String> getCorrect() {
        return new ArrayList<>(this.correct);
    }

    public ArrayList<String> getIncorrect() {
        return new ArrayList<>(this.incorrect);
    }

    public ArrayList<String> getUnanswered() {
        return new ArrayList<>(this.unanswered);
    }


}

class QuizResultFormatter {
    public static String format(QuizResult results) {
        int score = results.getScore();
        int size = results.getSize();
        ArrayList<String> unanswered = results.getUnanswered();
        ArrayList<String> correct = results.getCorrect();
        ArrayList<String> incorrect = results.getIncorrect();
        ArrayList<String> printedContent = new ArrayList<>();
        if (!unanswered.isEmpty()) {
            printedContent.add("You have not yet answered questions " + String.join(", ", unanswered));
        } else {
            if (!correct.isEmpty()) {
                printedContent.add("You correctly answered questions " + String.join(", ", correct));
            }
            if (!incorrect.isEmpty()) {
                printedContent.add("You incorrectly answered questions " + String.join(", ", incorrect));
            }

            printedContent.add("Your score is " + score + "/" + size);
            printedContent.add("\n");
        }
        return String.join(". ", printedContent);
    }
}