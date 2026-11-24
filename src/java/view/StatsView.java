package view;

import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsViewModel;
import interface_adapter.stats.StatsViewState;
import java.awt.Component;
import java.awt.Font;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public final class StatsView {
    private final JLabel summaryLabel = new JLabel(" ");
    private final DefaultListModel<String> topScores = new DefaultListModel<>();
    private final DefaultListModel<String> categoryAverages = new DefaultListModel<>();
    final JFrame frame = new JFrame("Stats");
    private final StatsController controller;

    public StatsView(StatsController controller, StatsViewModel viewModel) {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(viewModel);
        viewModel.addPropertyChangeListener(event -> render((StatsViewState) event.getNewValue()));
        this.controller = controller;

        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel title = new JLabel("High Scores & Statistics");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
            JButton refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(event -> controller.viewStats());

            addLeft(panel, title);
            addLeft(panel, summaryLabel);
            addLeft(panel, new JLabel("Top 10 scores:"));
            addLeft(panel, new JScrollPane(new JList<>(topScores)));
            addLeft(panel, new JLabel("Average by category:"));
            addLeft(panel, new JScrollPane(new JList<>(categoryAverages)));
            addLeft(panel, refreshButton);


            frame.add(panel);
            frame.setSize(400, 450);
//            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            controller.viewStats();
        });
    }

    public void loadView() {
        controller.viewStats();
        frame.setVisible(true);
    }

    private void render(StatsViewState state) {
        summaryLabel.setText(state.summary().isEmpty() ? " " : state.summary());
        topScores.clear();
        state.topScores().forEach(topScores::addElement);
        categoryAverages.clear();
        state.categoryAverages().forEach(categoryAverages::addElement);
    }

    private static void addLeft(JPanel panel, JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(component);
    }
}
