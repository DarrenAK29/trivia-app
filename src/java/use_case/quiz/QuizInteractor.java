package use_case.quiz;

import entity.*;

import java.util.*;

/** Coordinates the quiz; contains no Swing, printing, or database code. */
public final class QuizInteractor implements QuizInputBoundary {
    private final QuizSessionDataAccessInterface sessionDataAccess;
    private final QuestionDataAccessInterface questionDataAccess;
    private final QuizOutputBoundary presenter;
    private final ScoreSaveDataAccessInterface scores;

    public QuizInteractor(QuizSessionDataAccessInterface sessions, QuestionDataAccessInterface questionDataAccess, QuizOutputBoundary presenter,
                          ScoreSaveDataAccessInterface scores) {
        this.sessionDataAccess = Objects.requireNonNull(sessions);
        this.presenter = Objects.requireNonNull(presenter);
        this.scores = Objects.requireNonNull(scores);
        this.questionDataAccess = questionDataAccess;

    }

    public void startNewQuiz(QuizConfiguration config) {
        HashMap<String, String> typeApiToActual = new HashMap<>(Map.of("", "Any", "multiple", "Multiple Choice", "boolean", "True/False"));

        Objects.requireNonNull(config, "Quiz configuration cannot be null.");

        ConfigureQuizOutputData outputData;

        // 1. Fetch questions using the user's configuration.
        ArrayList<Question> questions =
                questionDataAccess.fetchQuestions(config);

        // 2. Check that the API returned questions.
        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException(
                    "No questions were returned. Please try another configuration."
            );
        }

        // 3. Convert the configuration values into quiz metadata.
        String difficulty = config.getDifficulty().getApiValue();
        String category = config.getCategoryId() != null ? String.valueOf(config.getCategoryId()) : "Any";
        String type = typeApiToActual.get(config.getQuestionType().getApiValue());

        // 4. Create the Quiz entity.
        Quiz quiz = new Quiz(
                difficulty,
                questions,
                category,
                type
        );

        // 5. Create a new QuizSession with no answers selected.
        // Session ID is 0 because the current DAO stores one session.
        QuizSession session = new QuizSession(quiz, 0);

        // 6. Save the generated session.
        sessionDataAccess.saveSession(session);


        // 7. Prepare the output for the presenter.
//        outputData = new ConfigureQuizOutputData(session);

        // 8. Notify the presenter that the quiz was created successfully.
        startQuiz();
    }

    @Override
    public void startQuiz() {
        present(sessionDataAccess.loadSession(), true, null, false);
    }


    @Override
    public void nextQuestion(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        session.nextQuestion();
        sessionDataAccess.saveSession(session);
        present(session, true, null, false);
    }

    @Override
    public void previousQuestion(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        session.prevQuestion();
        sessionDataAccess.saveSession(session);
        present(session, true, null, false);
    }

    @Override
    public void submitQuiz(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        sessionDataAccess.saveSession(session);
        QuizResult result = session.submissionResult();
        int unanswered = result.getUnanswered().size();
        QuizSubmissionData submission;
        if (unanswered > 0) {
            // Incomplete submissions do not disclose intermediate scores.
            submission = new QuizSubmissionData(unanswered, 0, result.getSize(), List.of(), List.of());
        } else {
            submission = new QuizSubmissionData(0, result.getScore(), result.getSize(),
                    result.getCorrect(), result.getIncorrect());
            scores.saveScore(new ScoreRecord(session.getQuiz().getCategory(),
                    session.getQuiz().getDifficultyLevel(), result.getScore(), result.getSize()));
        }
        present(session, true, submission, false);
    }

    @Override
    public void exitQuiz(QuizInputData input) {
        QuizSession session = saveAnswer(input);
        sessionDataAccess.saveSession(session);
        present(session, false, null, true);
    }

    private QuizSession saveAnswer(QuizInputData input) {
        Objects.requireNonNull(input, "input");
        QuizSession session = sessionDataAccess.loadSession();
        session.editSelectedChoices(session.getCurIndex(), input.selectedChoice());
        return session;
    }

    private void present(QuizSession session, boolean inQuiz,
                         QuizSubmissionData submission, boolean exiting) {
        List<QuestionOutputData> questions = new ArrayList<>();
        for (int i = 0; i < session.getQuiz().getQuestionCount(); i++) {
            Question question = session.getQuiz().getQuestion(i);
            questions.add(new QuestionOutputData(question.getQuestionContent(),
                    question.getChoices(), session.getSelectedChoice(i)));
        }
        presenter.present(new QuizOutputData(inQuiz, session.getCurIndex(),
                questions, submission, exiting));
    }
}
