package app;

import data_access.FileScoreDataAccessObject;
import data_access.InMemoryQuizSessionDataAccessObject;
import data_access.SampleQuizData;
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.QuizViewModel;
import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsPresenter;
import interface_adapter.stats.StatsViewModel;
import use_case.quiz.QuizInputBoundary;
import use_case.quiz.QuizInteractor;
import use_case.quiz.QuizOutputBoundary;
import use_case.quiz.QuizSessionDataAccessInterface;
import use_case.stats.StatsInteractor;
import view.QuizView;
import view.StatsView;

/** Composition root: the only place where all architectural layers are wired. */
public final class Main {
    public static void main(String[] args) {
        QuizSessionDataAccessInterface sessions = new InMemoryQuizSessionDataAccessObject(
                SampleQuizData.createSession());
        QuizViewModel viewModel = new QuizViewModel();
        QuizOutputBoundary presenter = new QuizPresenter(viewModel);
        FileScoreDataAccessObject scores = new FileScoreDataAccessObject("scores.txt");
        QuizInputBoundary interactor = new QuizInteractor(sessions, presenter, scores);
        QuizController controller = new QuizController(interactor);
        new QuizView(controller, viewModel);

        StatsViewModel statsViewModel = new StatsViewModel();
        StatsController statsController = new StatsController(
                new StatsInteractor(scores, new StatsPresenter(statsViewModel)));
        new StatsView(statsController, statsViewModel);
    }
}
