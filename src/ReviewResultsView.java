import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ReviewResultsView extends JPanel {
    private final JLabel scoreLabel;
    private final JTextArea reviewArea;

    public ReviewResultsView(
            ReviewResultsViewModel viewModel,
            Runnable backToHome) {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        scoreLabel = new JLabel(viewModel.getScoreText());
        add(scoreLabel, BorderLayout.NORTH);

        reviewArea = new JTextArea(viewModel.getReviewText());
        reviewArea.setEditable(false);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(reviewArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton homeButton = new JButton("Back to Home");
        homeButton.addActionListener(event -> backToHome.run());
        add(homeButton, BorderLayout.SOUTH);

        viewModel.addPropertyChangeListener(event -> {
            scoreLabel.setText(viewModel.getScoreText());
            reviewArea.setText(viewModel.getReviewText());
            reviewArea.setCaretPosition(0);
        });
    }
}