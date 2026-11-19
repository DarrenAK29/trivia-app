import use_case.quiz.QuizSessionDataAccessInterface;

import entity.Question;
import entity.Quiz;
import entity.QuizSession;
import entity.QuizResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

public class QuizView {
    private JFrame frame;
    private JPanel home;
    private final QuizSessionDataAccessInterface sessions;

    public static int getSelectedButtonIndex(ButtonGroup buttonGroup) {
        int index = 0;
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static void updateNavigationButtons(
            QuizSession qs,
            JButton backButton,
            JButton forwardButton,
            JButton submitButton) {

        int index = qs.getCurIndex();
        int lastIndex = qs.getQuiz().getQuestionCount() - 1;

        backButton.setVisible(index > 0);
        forwardButton.setVisible(index < lastIndex);
        submitButton.setVisible(index == lastIndex);
    }

    private void saveCurrentAnswer(
            QuizSession qs,
            ArrayList<ButtonGroup> buttonGroups) {

        int index = qs.getCurIndex();
        qs.editSelectedChoices(
                index,
                getSelectedButtonIndex(buttonGroups.get(index))
        );
        sessions.saveSession(qs);
    }

    private JPanel createQuizPanel(QuizSession qs) {
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        ArrayList<JPanel> questionPanels = new ArrayList<>();
        ArrayList<ButtonGroup> buttonGroups = new ArrayList<>();

        int startingIndex = qs.getCurIndex();
        Quiz quiz = qs.getQuiz();

        for (int i = 0; i < quiz.getQuestionCount(); i++) {
            Question question = quiz.getQuestion(i);
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            JLabel label = new JLabel(question.getQuestionContent());
            questionPanel.add(label);
            ButtonGroup options = new ButtonGroup();
            ArrayList<String> choices = question.getChoices();
            for (int j = 0; j < question.getChoiceCount(); j++) {
                JRadioButton option = new JRadioButton(choices.get(j));
                if (j == qs.getSelectedChoice(i)) {
                    option.setSelected(true);
                }
                options.add(option);
                questionPanel.add(option);
            }
            buttonGroups.add(options);
            questionPanel.setVisible(false);
            questionPanels.add(questionPanel);
        }

        JButton forwardButton = new JButton("Next");
        JButton backButton = new JButton("Previous");
        JButton submitButton = new JButton("Submit");
        JButton exitButton = new JButton("Exit");

        questionPanels.get(startingIndex).setVisible(true);
        int questionCount = qs.getQuiz().getQuestionCount();

        updateNavigationButtons(qs, backButton, forwardButton, submitButton);



        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer(qs, buttonGroups);
                int prevInd = qs.getCurIndex();
                questionPanels.get(prevInd).setVisible(false);
                qs.nextQuestion();
                int newInd = qs.getCurIndex();
                questionPanels.get(newInd).setVisible(true);
                updateNavigationButtons(qs, backButton, forwardButton, submitButton);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer(qs, buttonGroups);
                int curInd = qs.getCurIndex();
                questionPanels.get(curInd).setVisible(false);
                qs.prevQuestion();
                int newInd = qs.getCurIndex();
                questionPanels.get(newInd).setVisible(true);
                updateNavigationButtons(qs, backButton, forwardButton, submitButton);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer(qs, buttonGroups);
                int unansweredQuestions = 0;
                for (int i = 0; i < questionCount; i++) {
                    if (qs.getSelectedChoice(i) == -1) {
                        unansweredQuestions++;
                    }
                }
                if (unansweredQuestions == 0) {
                    QuizResult results = qs.submissionResult();
                    System.out.println(QuizResultFormatter.format(results));
                } else {
                    System.out.println("You have " + unansweredQuestions +" unanswered questions left!");
                }

            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer(qs, buttonGroups);
                System.out.println(qs);
                frame.setTitle("Home");
                quizPanel.setVisible(false);
                home.setVisible(true);

                frame.remove(quizPanel);
                frame.revalidate();
                frame.repaint();

            }
        });

        for (JPanel panel: questionPanels) {
            quizPanel.add(panel);
        }

        JPanel navigationButtons = new JPanel();
        navigationButtons.setLayout(new FlowLayout());
        navigationButtons.add(backButton);
        navigationButtons.add(forwardButton);
        navigationButtons.add(submitButton);
        quizPanel.add(navigationButtons);
        quizPanel.add(exitButton);

        return quizPanel;
    }

    public QuizView(QuizSessionDataAccessInterface sessions) {
        this.sessions = sessions;
        SwingUtilities.invokeLater(() -> {
            QuizSession qs = sessions.loadSession();

            frame = new JFrame("Home page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

            home = new JPanel();
            home.setLayout(new FlowLayout());
            JButton startButton = new JButton("Start quiz");
            home.add(startButton);
            home.setVisible(true);
            frame.add(home);

            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JPanel quizPanel = createQuizPanel(qs);
                    frame.add(quizPanel);
                    home.setVisible(false);
                    quizPanel.setVisible(true);
                    frame.setTitle("Quiz!");
                }
            });

            frame.setVisible(true);
        });
    }
}
