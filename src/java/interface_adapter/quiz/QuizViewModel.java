package interface_adapter.quiz;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/** Presenter writes to this; the Swing view observes it. */
public final class QuizViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private QuizViewState state = QuizViewState.home();

    public QuizViewState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("state", listener);
    }

    public void setState(QuizViewState newState) {
        this.state = Objects.requireNonNull(newState);
        // Always notify: submitting the same answers twice should print twice.
        changes.firePropertyChange("state", null, newState);
    }
}
