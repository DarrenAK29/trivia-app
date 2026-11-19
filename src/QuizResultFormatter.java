import java.util.ArrayList;

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
