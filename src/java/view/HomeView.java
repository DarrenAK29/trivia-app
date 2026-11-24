package view;

import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizViewModel;
import use_case.quiz.QuizConfigFileDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HomeView {

    JFrame frame;
    QuizController quizController;
    QuizViewModel quizViewModel;
    QuizConfigFileDataAccessInterface qcfdai;
    StatsView statsView;
    ConfigureQuizView configureQuizView;

    private static void customizeButton(JButton button, Dimension dimensions, Font font) {
        button.setPreferredSize(dimensions);
        button.setMaximumSize(dimensions);
        button.setMinimumSize(dimensions);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(font);
    }

    public HomeView(
//            ConfigureQuizController configureController,
//            ConfigureQuizViewModel configureViewModel,
//            QuizController quizController,
//            QuizViewModel quizViewModel
            StatsView statsView,
            ConfigureQuizView configureQuizView,
            QuizController quizController,
            QuizViewModel quizViewModel,
            QuizConfigFileDataAccessInterface qcfdai

    ) {

//        this.configureController = Objects.requireNonNull(configureController);
//        this.configureViewModel = Objects.requireNonNull(configureViewModel);
//        this.quizController = Objects.requireNonNull(quizController);
//        this.quizViewModel = Objects.requireNonNull(quizViewModel);

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Home");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(550, 500);
            frame.setMinimumSize(new Dimension(450, 450));
            frame.setLocationRelativeTo(null);
            this.quizController = quizController;
            this.quizViewModel = quizViewModel;
            this.qcfdai = qcfdai;
            this.statsView = statsView;
            this.configureQuizView = configureQuizView;

            buildView();

//            configureViewModel.addPropertyChangeListener(
//                    event -> render(
//                            (ConfigureQuizViewState) event.getNewValue()
//                    )
//            );
//
//            render(configureViewModel.getState());

            frame.setVisible(true);
        });
    }

    private void buildView() {
        final int MIN_BUTTON_SIZE = 100;
        final int BUTTON_HEIGHT = 50;
        final double BUTTON_SCALER = 0.45;
        final int BUTTON_SPACING = 20;
        final int QUESTION_UPDOWN_MARGIN = 40;

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel titleLabel = new JLabel("Welcome to the Trivia App!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(QUESTION_UPDOWN_MARGIN, 0, QUESTION_UPDOWN_MARGIN, 0)
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        homePanel.add(titleLabel);

        Color homeBtnCollor = new Color(20, 80, 150);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Dimension startingSizes = new Dimension(Math.max(MIN_BUTTON_SIZE, (int) (frame.getWidth() * BUTTON_SCALER)), BUTTON_HEIGHT);

        RoundedButton startButton = new RoundedButton("Start Quiz", homeBtnCollor, 20);
        customizeButton(startButton, startingSizes, buttonFont);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new QuizView(quizController, quizViewModel);
                quizController.newQuiz(qcfdai.loadConfig());

            }
        });

        RoundedButton configButton = new RoundedButton("Configure Settings", homeBtnCollor, 20);
        customizeButton(configButton, startingSizes, buttonFont);
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureQuizView.loadView();
            }
        });

        RoundedButton statsButton = new RoundedButton("View Stats", homeBtnCollor, 20);
        customizeButton(statsButton, startingSizes, buttonFont);
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statsView.loadView();
            }
        });

        RoundedButton continueButton = new RoundedButton("Continue Past Session", homeBtnCollor, 20);
        customizeButton(continueButton, startingSizes, buttonFont);

        homePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = Math.max(MIN_BUTTON_SIZE, (int) (frame.getWidth() * BUTTON_SCALER));

                for (Component component : homePanel.getComponents()) {
                    if (component instanceof RoundedButton button) {
                        Dimension size = new Dimension(width, BUTTON_HEIGHT);
                        customizeButton(button, size, buttonFont);
                    }
                }

                frame.revalidate();
                frame.repaint();
            }
        });

        homePanel.add(startButton);
        homePanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        homePanel.add(configButton);
        homePanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        homePanel.add(statsButton);
        homePanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        homePanel.add(continueButton);
        homePanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        frame.add(homePanel);



    }
}
