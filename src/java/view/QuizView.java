package view;

import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizQuestionState;
import interface_adapter.quiz.QuizViewModel;
import interface_adapter.quiz.QuizViewState;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

/** Swing details only: no quiz entities, grading, or data access. */
public final class QuizView {
    private final QuizController controller;
    private final QuizViewModel viewModel;
    private JFrame frame;
    private boolean hasOpenedQuiz;

    public QuizView(QuizController controller, QuizViewModel viewModel) {
        this.controller = Objects.requireNonNull(controller);
        this.viewModel = Objects.requireNonNull(viewModel);
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Home page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            viewModel.addPropertyChangeListener(event -> render((QuizViewState) event.getNewValue()));
            render(viewModel.getState());
            frame.setVisible(true);
        });
    }

    private void render(QuizViewState state) {
        frame.getContentPane().removeAll();
        if (state.inQuiz()) {
            showQuiz(state);
        } else {
            showHome();
        }
        frame.revalidate();
        frame.repaint();
        // Preserve the prototype's console output, while keeping formatting out of Swing.
        if (state.message() != null) {
            System.out.println(state.message());
        }
    }

    private void showHome() {
        // The prototype displays "Home page" initially and "Home" after exiting.
        frame.setTitle(hasOpenedQuiz ? "Home" : "Home page");
        JPanel home = new JPanel(new FlowLayout());
        JButton startButton = new JButton("Start quiz");
        startButton.addActionListener(event -> controller.startQuiz());
        home.add(startButton);
        frame.add(home);
    }

    private void showQuiz(QuizViewState state) {
        hasOpenedQuiz = true;
        frame.setTitle("Quiz!");
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        QuizQuestionState current = state.questions().get(state.currentIndex());
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.add(new JLabel(current.text()));

        ButtonGroup options = new ButtonGroup();
        for (int i = 0; i < current.choices().size(); i++) {
            JRadioButton option = new JRadioButton(current.choices().get(i));
            option.setSelected(i == current.selectedChoice());
            options.add(option);
            questionPanel.add(option);
        }
        quizPanel.add(questionPanel);

        JButton backButton = new JButton("Previous");
        JButton forwardButton = new JButton("Next");
        JButton submitButton = new JButton("Submit");
        JButton exitButton = new JButton("Exit");

        int lastIndex = state.questions().size() - 1;
        backButton.setVisible(state.currentIndex() > 0);
        forwardButton.setVisible(state.currentIndex() < lastIndex);
        submitButton.setVisible(state.currentIndex() == lastIndex);

        backButton.addActionListener(event -> controller.previousQuestion(selectedIndex(options)));
        forwardButton.addActionListener(event -> controller.nextQuestion(selectedIndex(options)));
        submitButton.addActionListener(event -> controller.submitQuiz(selectedIndex(options)));
        exitButton.addActionListener(event -> controller.exitQuiz(selectedIndex(options)));

        JPanel navigationButtons = new JPanel(new FlowLayout());
        navigationButtons.add(backButton);
        navigationButtons.add(forwardButton);
        navigationButtons.add(submitButton);
        quizPanel.add(navigationButtons);
        quizPanel.add(exitButton);
        frame.add(quizPanel);
    }

    private static int selectedIndex(ButtonGroup options) {
        int index = 0;
        for (Enumeration<AbstractButton> buttons = options.getElements(); buttons.hasMoreElements();) {
            if (buttons.nextElement().isSelected()) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
