package entity;

import java.util.Arrays;

public class QuizSession {
    private Quiz quiz;
    private int curIndex;
    private int[] selectedChoices;
    private int id;

    public QuizSession(Quiz quiz, int[] selectedChoices, int curIndex, int id) {
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
        this.id = id;
    }

    public QuizSession(Quiz quiz, int id) { this(quiz, createEmptyChoices(quiz.getQuestionCount()), 0, id); }

    public QuizSession(Quiz quiz, int[] selectedChoices, int id) {
        this(quiz, selectedChoices, 0, id);
    }

    public QuizSession(Quiz quiz, int curIndex, int id) {
        this(quiz, createEmptyChoices(quiz.getQuestionCount()), curIndex, id);
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

    @Override
    public String toString() {
        return "Selected choices are " + Arrays.toString(selectedChoices) + ". Currently on index " + curIndex;
    }
}
