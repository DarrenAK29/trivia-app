package view;

import entity.QuizConfiguration;
import interface_adapter.quiz.ConfigureQuizController;
import interface_adapter.quiz.ConfigureQuizViewModel;
import interface_adapter.quiz.ConfigureQuizViewState;
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizViewModel;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

/**
 * Swing view for configuring a new quiz.
 */
public final class ConfigureQuizView {

    private final ConfigureQuizController configureController;
    private final ConfigureQuizViewModel configureViewModel;
    private final QuizController quizController;
    private final QuizViewModel quizViewModel;

    private JFrame frame;

    private JSpinner questionCountSpinner;
    private JComboBox<CategoryOption> categoryBox;
    private JComboBox<String> difficultyBox;
    private JComboBox<String> questionTypeBox;
    private JComboBox<String> encodingBox;
    private JButton generateButton;
    private JLabel errorLabel;

    private boolean hasLaunchedQuiz;

    public ConfigureQuizView(
            ConfigureQuizController configureController,
            ConfigureQuizViewModel configureViewModel,
            QuizController quizController,
            QuizViewModel quizViewModel) {

        this.configureController = Objects.requireNonNull(configureController);
        this.configureViewModel = Objects.requireNonNull(configureViewModel);
        this.quizController = Objects.requireNonNull(quizController);
        this.quizViewModel = Objects.requireNonNull(quizViewModel);

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Configure Quiz");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(550, 500);
            frame.setMinimumSize(new Dimension(450, 450));
            frame.setLocationRelativeTo(null);

            buildView();

            configureViewModel.addPropertyChangeListener(
                    event -> render(
                            (ConfigureQuizViewState) event.getNewValue()
                    )
            );

            render(configureViewModel.getState());

            frame.setVisible(true);
        });
    }

    private void buildView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 45, 25, 45)
        );

        JLabel title = new JLabel("Configure Your Quiz");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose your preferred quiz settings");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(10, 5, 10, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.weightx = 0.4;

        constraints.gridy = 0;
        addFormRow(
                formPanel, constraints,
                "Number of questions:",
                createQuestionCountSpinner()
        );

        constraints.gridy = 1;
        addFormRow(
                formPanel, constraints,
                "Category:",
                createCategoryBox()
        );

        constraints.gridy = 2;
        addFormRow(
                formPanel, constraints,
                "Difficulty:",
                createDifficultyBox()
        );

        constraints.gridy = 3;
        addFormRow(
                formPanel, constraints,
                "Question type:",
                createQuestionTypeBox()
        );

        constraints.gridy = 4;
        addFormRow(
                formPanel, constraints,
                "Answer encoding:",
                createEncodingBox()
        );

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        generateButton = new JButton("Generate Quiz");
        generateButton.setFont(new Font("Arial", Font.BOLD, 16));
        generateButton.setBackground(new Color(45, 130, 215));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateButton.setMaximumSize(new Dimension(200, 45));

        generateButton.addActionListener(event -> generateQuiz());

        mainPanel.add(generateButton);
        mainPanel.add(Box.createVerticalStrut(15));

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(errorLabel);

        frame.add(mainPanel);
    }

    private JSpinner createQuestionCountSpinner() {
        questionCountSpinner = new JSpinner(
                new SpinnerNumberModel(10, 1, 50, 1)
        );
        return questionCountSpinner;
    }

    private JComboBox<CategoryOption> createCategoryBox() {
        CategoryOption[] categories = {
                new CategoryOption("Any Category", null),
                new CategoryOption("General Knowledge", 9),
                new CategoryOption("Books", 10),
                new CategoryOption("Film", 11),
                new CategoryOption("Music", 12),
                new CategoryOption("Musicals & Theatres", 13),
                new CategoryOption("Television", 14),
                new CategoryOption("Video Games", 15),
                new CategoryOption("Board Games", 16),
                new CategoryOption("Science & Nature", 17),
                new CategoryOption("Computers", 18),
                new CategoryOption("Mathematics", 19),
                new CategoryOption("Mythology", 20),
                new CategoryOption("Sports", 21),
                new CategoryOption("Geography", 22),
                new CategoryOption("History", 23),
                new CategoryOption("Politics", 24),
                new CategoryOption("Art", 25),
                new CategoryOption("Celebrities", 26),
                new CategoryOption("Animals", 27),
                new CategoryOption("Vehicles", 28),
                new CategoryOption("Comics", 29),
                new CategoryOption("Gadgets", 30),
                new CategoryOption("Japanese Anime & Manga", 31),
                new CategoryOption("Cartoon & Animations", 32)
        };

        categoryBox = new JComboBox<>(categories);
        return categoryBox;
    }

    private JComboBox<String> createDifficultyBox() {
        String[] difficulties = {
                "Any Difficulty",
                "Easy",
                "Medium",
                "Hard"
        };

        difficultyBox = new JComboBox<>(difficulties);
        return difficultyBox;
    }

    private JComboBox<String> createQuestionTypeBox() {
        String[] types = {
                "Any Type",
                "Multiple Choice",
                "True / False"
        };

        questionTypeBox = new JComboBox<>(types);
        return questionTypeBox;
    }

    private JComboBox<String> createEncodingBox() {
        String[] encodings = {
                "Default",
                "URL Safe",
                "Base64"
        };

        encodingBox = new JComboBox<>(encodings);
        return encodingBox;
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints constraints,
            String labelText,
            JComponent input) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        constraints.gridx = 0;
        constraints.weightx = 0.4;
        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.6;
        panel.add(input, constraints);
    }

    private void generateQuiz() {
        errorLabel.setText(" ");

        int questionCount = (Integer) questionCountSpinner.getValue();

        CategoryOption selectedCategory =
                (CategoryOption) categoryBox.getSelectedItem();

        Integer categoryId = selectedCategory == null
                ? null
                : selectedCategory.getId();

        QuizConfiguration.Difficulty difficulty =
                QuizConfiguration.Difficulty.values()[
                        difficultyBox.getSelectedIndex()
                        ];

        QuizConfiguration.QuestionType questionType =
                QuizConfiguration.QuestionType.values()[
                        questionTypeBox.getSelectedIndex()
                        ];

        QuizConfiguration.Encoding encoding =
                QuizConfiguration.Encoding.values()[
                        encodingBox.getSelectedIndex()
                        ];

        QuizConfiguration config = new QuizConfiguration(
                questionCount,
                categoryId,
                difficulty,
                questionType,
                encoding
        );

        generateButton.setEnabled(false);
        generateButton.setText("Generating...");

        configureController.configureQuiz(config);
    }

    private void render(ConfigureQuizViewState state) {
        if (state.isSuccessful()) {
            if (!hasLaunchedQuiz) {
                hasLaunchedQuiz = true;

                frame.dispose();

                // Open the existing gameplay screen.
                new QuizView(quizController, quizViewModel);
                quizController.startQuiz();
            }
        } else {
            generateButton.setEnabled(true);
            generateButton.setText("Generate Quiz");

            if (state.getError() != null) {
                errorLabel.setText(state.getError());
            }
        }
    }

    /**
     * Represents a category shown in the category dropdown.
     */
    private static final class CategoryOption {

        private final String name;
        private final Integer id;

        private CategoryOption(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
