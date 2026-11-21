package data_access;

import use_case.quiz.QuestionDataAccessInterface;
import entity.QuizConfiguration;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Collections;
import entity.Question;

public class OpenTriviaApiDataAccessObject
        implements QuestionDataAccessInterface {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private String buildUrl(QuizConfiguration config) {
        StringBuilder url = new StringBuilder(
                "https://opentdb.com/api.php?amount="
        );

        url.append(config.getQuestionCount());

        if (config.getCategoryId() != null) {
            url.append("&category=").append(config.getCategoryId());
        }

        if (!config.getDifficulty().getApiValue().isEmpty()) {
            url.append("&difficulty=")
                    .append(config.getDifficulty().getApiValue());
        }

        if (!config.getQuestionType().getApiValue().isEmpty()) {
            url.append("&type=")
                    .append(config.getQuestionType().getApiValue());
        }

        if (!config.getEncoding().getApiValue().equals("default")) {
            url.append("&encode=")
                    .append(config.getEncoding().getApiValue());
        }

        return url.toString();
    }

    private HttpRequest buildRequest(QuizConfiguration config) {
        String url = buildUrl(config);

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    private HttpResponse<String> sendRequest(QuizConfiguration config)
            throws IOException, InterruptedException {

        HttpRequest request = buildRequest(config);

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "API request failed with status code: " + response.statusCode()
            );
        }

        return response;
    }

    private void checkApiResponse(JsonObject response) {
        int responseCode = response.get("response_code").getAsInt();

        if (responseCode != 0) {
            throw new RuntimeException(
                    "Open Trivia DB returned error code: " + responseCode
            );
        }
    }

    private JsonArray getResults(String json) {
        JsonObject response = JsonParser.parseString(json).getAsJsonObject();

        checkApiResponse(response);

        return response.getAsJsonArray("results");
    }

    private String getQuestionText(JsonObject question) {
        return question.get("question").getAsString();
    }

    private String getCorrectAnswer(JsonObject question) {
        return question.get("correct_answer").getAsString();
    }

    private ArrayList<String> getIncorrectAnswers(JsonObject question) {
        JsonArray incorrectAnswers = question.getAsJsonArray("incorrect_answers");

        ArrayList<String> answers = new ArrayList<>();

        for (int i = 0; i < incorrectAnswers.size(); i++) {
            answers.add(incorrectAnswers.get(i).getAsString());
        }

        return answers;
    }

    private ArrayList<String> getChoices(JsonObject question) {
        ArrayList<String> choices = getIncorrectAnswers(question);

        String correctAnswer = getCorrectAnswer(question);
        choices.add(correctAnswer);

        return choices;
    }

    private int shuffleChoices(ArrayList<String> choices, String correctAnswer) {
        Collections.shuffle(choices);

        return choices.indexOf(correctAnswer);
    }

    private Question createQuestion(JsonObject questionData) {
        String questionText = getQuestionText(questionData);

        String correctAnswer = getCorrectAnswer(questionData);

        ArrayList<String> choices = getChoices(questionData);

        int correctChoice = shuffleChoices(choices, correctAnswer);

        return new Question(questionText, choices, correctChoice);
    }

    private ArrayList<Question> createQuestions(JsonArray results) {
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            JsonObject questionData = results.get(i).getAsJsonObject();

            questions.add(createQuestion(questionData));
        }

        return questions;
    }

    @Override
    public ArrayList<Question> fetchQuestions(QuizConfiguration config) {
        try {
            HttpResponse<String> response = sendRequest(config);

            String json = response.body();

            JsonArray results = getResults(json);

            return createQuestions(results);

        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve questions from API.", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Question retrieval was interrupted.", e);
        }
    }

}