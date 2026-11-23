package app;

import data_access.*;

import interface_adapter.quiz.ConfigureQuizController;
import interface_adapter.quiz.ConfigureQuizPresenter;
import interface_adapter.quiz.ConfigureQuizViewModel;
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.QuizViewModel;

import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsPresenter;
import interface_adapter.stats.StatsViewModel;

import use_case.quiz.*;

import use_case.stats.StatsInteractor;

import view.ConfigureQuizView;
import view.HomeView;
import view.StatsView;

/**
 * Composition root: the only place where all architectural layers are wired.
 */
public final class Main {

    public static void main(String[] args) {

        // Shared session storage.
        QuizSessionDataAccessInterface sessions =
                new InMemoryQuizSessionDataAccessObject(
                        SampleQuizData.createSession()
                );

        QuestionDataAccessInterface questionDataAccess = new OpenTriviaApiDataAccessObject();

        // Existing gameplay setup.
        QuizViewModel quizViewModel = new QuizViewModel();

        QuizOutputBoundary quizPresenter = new QuizPresenter(quizViewModel);

        FileScoreDataAccessObject scores = new FileScoreDataAccessObject("scores.txt");

        QuizConfigFileDataAccessInterface quizSettings = new QuizConfigFileDataAccessObject("quizsettings.txt");

        QuizInputBoundary quizInteractor = new QuizInteractor(sessions, questionDataAccess, quizPresenter, scores);

        QuizController quizController = new QuizController(quizInteractor);

        // New quiz configuration setup.
        ConfigureQuizViewModel configureViewModel = new ConfigureQuizViewModel();

        ConfigureQuizOutputBoundary configurePresenter = new ConfigureQuizPresenter(configureViewModel);

        ConfigureQuizInputBoundary configureInteractor = new ConfigureQuizInteractor(quizSettings, questionDataAccess, sessions, configurePresenter);

        ConfigureQuizController configureController = new ConfigureQuizController(configureInteractor);

        // Existing statistics setup.
        StatsViewModel statsViewModel = new StatsViewModel();

        StatsController statsController =
                new StatsController(
                        new StatsInteractor(
                                scores,
                                new StatsPresenter(statsViewModel)
                        )
                );

//        new StatsView(statsController, statsViewModel);

        new HomeView(statsController, statsViewModel, configureController, configureViewModel, quizController, quizViewModel, quizSettings);
    }
}
