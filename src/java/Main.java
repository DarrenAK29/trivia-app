import data_access.InMemoryQuizSessionDataAccessObject;
import data_access.SampleQuizData;

public class Main {
    public static void main(String[] args) {
        new QuizView(new InMemoryQuizSessionDataAccessObject(SampleQuizData.createSession()));
    }
}
