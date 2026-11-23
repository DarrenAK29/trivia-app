package view;

import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizQuestionState;
import interface_adapter.quiz.QuizViewModel;
import interface_adapter.quiz.QuizViewState;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.*;

class CustomToggleButton extends JToggleButton {
    private final int arc = 12;

    public CustomToggleButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false); // Disable default border painting

//        setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
//        revalidate();
//        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected()) {
            g2.setColor(new Color(45, 130, 215));
            setForeground(Color.WHITE);
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(215, 215, 215));
            setForeground(Color.BLACK);
        } else {
            g2.setColor(new Color(235, 235, 235));
            setForeground(Color.BLACK);
        }
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(isSelected() ? new Color(20, 80, 150) : new Color(180, 180, 180));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}

class RoundedButton extends JButton {

    private final int radius;

    public RoundedButton(String text, Color color, int radius) {
        super(text);
        this.radius = radius;

        setBackground(color);
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Draw rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(
                0, 0,
                getWidth(), getHeight(),
                radius, radius
        );

        // Draw centered text
        g2.setFont(getFont());
        g2.setColor(getForeground());

        FontMetrics fm = g2.getFontMetrics();

        int textWidth = fm.stringWidth(getText());
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(getText(), x, y);

        g2.dispose();

    }
}

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
            frame.setSize(1100, 500);
            frame.setResizable(false);
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
            frame.setVisible(false);
        }
        frame.revalidate();
        frame.repaint();
        // Preserve the prototype's console output, while keeping formatting out of Swing.
        if (state.message() != null) {
            System.out.println(state.message());
        }
    }


    private static void changeButtonSize(JButton button, Dimension dimensions) {
        button.setPreferredSize(dimensions);
        button.setMaximumSize(dimensions);
        button.setMinimumSize(dimensions);
    }


    private void showQuiz(QuizViewState state) {
        hasOpenedQuiz = true;
        final int QUESTION_UPDOWN_MARGIN = 30;
        final int BUTTON_PANEL_SIDE_MARGIN = 100;
        final int BUTTON_HEIGHT = 45;
        final int MIN_BUTTON_SIZE = 100;
        final double BUTTON_SCALER = 0.7;
        final int BUTTON_SPACING = 10;

        frame.setTitle("Quiz!");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel quizPanel = new JPanel();

        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        QuizQuestionState current = state.questions().get(state.currentIndex());
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        String questionText = "Question " + (state.currentIndex() + 1) +": " + current.text();

        String labelText = "<html><div style='width: 100%; text-align: center;'>"
                + questionText
                + "</div></html>";

        JLabel questionLabel = new JLabel(labelText);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBorder(
                BorderFactory.createEmptyBorder(QUESTION_UPDOWN_MARGIN, 0, QUESTION_UPDOWN_MARGIN, 0)
        );

        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionPanel.add(questionLabel);


        ButtonGroup options = new ButtonGroup();
        JPanel choiceButtonPanel = new JPanel();
        choiceButtonPanel.setLayout(
                new BoxLayout(choiceButtonPanel, BoxLayout.Y_AXIS)
        );
//        choiceButtonPanel.setBackground(Color.GREEN);

        for (int i = 0; i < current.choices().size(); i++) {
            JToggleButton option = new CustomToggleButton(current.choices().get(i));

            Dimension size = new Dimension(Math.max(MIN_BUTTON_SIZE,
                    (int) (BUTTON_SCALER * choiceButtonPanel.getWidth())),
                    BUTTON_HEIGHT);

            option.setPreferredSize(size);
            option.setMinimumSize(size);
            option.setMaximumSize(size);

            option.setFont(new Font("Arial", Font.BOLD, 16));

            option.setAlignmentX(Component.CENTER_ALIGNMENT);

            option.setSelected(i == current.selectedChoice());
            options.add(option);

            // Center each button within the green panel.
            option.setAlignmentX(Component.CENTER_ALIGNMENT);
            choiceButtonPanel.add(option);
            if (i < current.choices().size() - 1) {
                choiceButtonPanel.add(Box.createVerticalStrut(BUTTON_SPACING));
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(
                BorderFactory.createEmptyBorder(0, BUTTON_PANEL_SIDE_MARGIN, 0, BUTTON_PANEL_SIDE_MARGIN)
        );
        wrapper.add(choiceButtonPanel, BorderLayout.CENTER);

// Let the wrapper expand horizontally.
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        wrapper.getPreferredSize().height
                )
        );

        questionPanel.add(wrapper);

        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, questionPanel.getPreferredSize().height)
        );


        Dimension backSize = new Dimension(150, 38);
        Dimension forwardSize = new Dimension(80, 38);
        Dimension submitSize = new Dimension(114, 38);
        Dimension exitSize = new Dimension(80, 38);
        Color navColor = new Color(11, 120, 113);
        Color submitColor = new Color(45, 185, 100);
        Color exitColor = new Color(240, 100, 100);

        RoundedButton backButton = new RoundedButton("Previous", navColor, 20);
        changeButtonSize(backButton, backSize);

        RoundedButton forwardButton = new RoundedButton("Next", navColor, 20);
        changeButtonSize(forwardButton, forwardSize);

        RoundedButton submitButton = new RoundedButton("Submit", submitColor, 20);
        changeButtonSize(submitButton, submitSize);

        RoundedButton exitButton = new RoundedButton("Exit", exitColor, 20);
        changeButtonSize(exitButton, exitSize);

        Component navHorizontalSpacing = Box.createHorizontalStrut(8);

        int lastIndex = state.questions().size() - 1;
        backButton.setVisible(state.currentIndex() > 0);
        navHorizontalSpacing.setVisible(state.currentIndex() > 0);
        forwardButton.setVisible(state.currentIndex() < lastIndex);
        submitButton.setVisible(state.currentIndex() == lastIndex);

        backButton.addActionListener(event -> controller.previousQuestion(selectedIndex(options)));
        forwardButton.addActionListener(event -> controller.nextQuestion(selectedIndex(options)));
        submitButton.addActionListener(event -> controller.submitQuiz(selectedIndex(options)));
        exitButton.addActionListener(event -> controller.exitQuiz(selectedIndex(options)));

        JPanel lowerButtonPanel = new JPanel();
        lowerButtonPanel.setLayout(new BoxLayout(lowerButtonPanel, BoxLayout.Y_AXIS));

        lowerButtonPanel.setLayout(
                new BoxLayout(lowerButtonPanel, BoxLayout.Y_AXIS)
        );

        JPanel navigationButtonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        navigationButtonPanel.add(backButton);
        navigationButtonPanel.add(navHorizontalSpacing);
        navigationButtonPanel.add(forwardButton);
        navigationButtonPanel.add(submitButton);

        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navigationButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lowerButtonPanel.setOpaque(false);
        lowerButtonPanel.add(navigationButtonPanel);
        lowerButtonPanel.add(Box.createVerticalStrut(2 * BUTTON_SPACING));
        lowerButtonPanel.add(exitButton);


        navigationButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navigationButtonPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        navigationButtonPanel.getPreferredSize().height
                )
        );
        lowerButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lowerButtonPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        lowerButtonPanel.getPreferredSize().height
                )
        );

        quizPanel.add(questionPanel);
        quizPanel.add(Box.createVerticalStrut(2 * BUTTON_SPACING));
        quizPanel.add(lowerButtonPanel);

        choiceButtonPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = Math.max(MIN_BUTTON_SIZE, (int) (choiceButtonPanel.getWidth() * BUTTON_SCALER));

                for (Component component : choiceButtonPanel.getComponents()) {
                    if (component instanceof JToggleButton button) {
                        Dimension size = new Dimension(width, BUTTON_HEIGHT);

                        button.setPreferredSize(size);
                        button.setMinimumSize(size);
                        button.setMaximumSize(size);
                    }
                }

                choiceButtonPanel.revalidate();
                choiceButtonPanel.repaint();
            }
        });

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
