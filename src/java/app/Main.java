package app;

import data_access.FileScoreDataAccessObject;
import data_access.InMemoryQuizSessionDataAccessObject;
import data_access.OpenTriviaApiDataAccessObject;
import data_access.SampleQuizData;

import interface_adapter.quiz.ConfigureQuizController;
import interface_adapter.quiz.ConfigureQuizPresenter;
import interface_adapter.quiz.ConfigureQuizViewModel;
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.QuizViewModel;

import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsPresenter;
import interface_adapter.stats.StatsViewModel;

import use_case.quiz.ConfigureQuizInputBoundary;
import use_case.quiz.ConfigureQuizInteractor;
import use_case.quiz.ConfigureQuizOutputBoundary;
import use_case.quiz.QuestionDataAccessInterface;
import use_case.quiz.QuizInputBoundary;
import use_case.quiz.QuizInteractor;
import use_case.quiz.QuizOutputBoundary;
import use_case.quiz.QuizSessionDataAccessInterface;

import use_case.stats.StatsInteractor;

import view.ConfigureQuizView;
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

        // Existing gameplay setup.
        QuizViewModel quizViewModel = new QuizViewModel();

        QuizOutputBoundary quizPresenter =
                new QuizPresenter(quizViewModel);

        FileScoreDataAccessObject scores =
                new FileScoreDataAccessObject("scores.txt");

        QuizInputBoundary quizInteractor =
                new QuizInteractor(
                        sessions,
                        quizPresenter,
                        scores
                );

        QuizController quizController =
                new QuizController(quizInteractor);


        // New quiz configuration setup.
        ConfigureQuizViewModel configureViewModel =
                new ConfigureQuizViewModel();

        ConfigureQuizOutputBoundary configurePresenter =
                new ConfigureQuizPresenter(configureViewModel);

        QuestionDataAccessInterface questionDataAccess =
                new OpenTriviaApiDataAccessObject();

        ConfigureQuizInputBoundary configureInteractor =
                new ConfigureQuizInteractor(
                        questionDataAccess,
                        sessions,
                        configurePresenter
                );

        ConfigureQuizController configureController =
                new ConfigureQuizController(configureInteractor);


        // Open configuration screen first.
        // The existing QuizView opens after successful configuration.
        new ConfigureQuizView(
                configureController,
                configureViewModel,
                quizController,
                quizViewModel
        );


        // Existing statistics setup.
        StatsViewModel statsViewModel = new StatsViewModel();

        StatsController statsController =
                new StatsController(
                        new StatsInteractor(
                                scores,
                                new StatsPresenter(statsViewModel)
                        )
                );

        new StatsView(statsController, statsViewModel);
    }
}
