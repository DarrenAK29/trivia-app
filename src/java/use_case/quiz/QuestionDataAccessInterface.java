package use_case.quiz;

import entity.Question;
import entity.QuizConfiguration;

import java.util.ArrayList;

/**
 * Data access interface for retrieving questions
 * based on a user's quiz configuration.
 */
public interface QuestionDataAccessInterface {
    ArrayList<Question> fetchQuestions(QuizConfiguration config);
}
