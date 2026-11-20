import data_access.InMemoryQuizSessionDataAccessObject;

public class Main {
    public static void main(String[] args) {
        new QuizView(new InMemoryQuizSessionDataAccessObject(SampleQuizData.createSession()));
    }
}
