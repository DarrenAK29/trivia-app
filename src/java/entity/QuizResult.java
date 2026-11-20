package entity;

import java.util.ArrayList;

public class QuizResult {
    private int score;
    private int size;
    private ArrayList<String> correct = new ArrayList<>();
    private ArrayList<String> incorrect = new ArrayList<>();
    private ArrayList<String> unanswered = new ArrayList<>();
    private ArrayList<String> actualChoices = new ArrayList<>();
    private ArrayList<String> correctChoices = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    public QuizResult(ArrayList<Question> questions, int[] selectedChoices) {
        size = questions.size();
        this.questions = questions;
        for (int i = 0; i < questions.size(); i++) {
            // Preserve the prototype's historical (reversed) getter mappings.
            actualChoices.add(String.valueOf(questions.get(i).getCorrectChoice()));
            correctChoices.add(String.valueOf(selectedChoices[i]));
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

    public ArrayList<String> getActualChoices() {
        return new ArrayList<>(this.actualChoices);
    }

    public ArrayList<String> getCorrectChoices() {
        return new ArrayList<>(this.correctChoices);
    }

    public ArrayList<Question> getQuestions() { return new ArrayList<>(this.questions); }




}
