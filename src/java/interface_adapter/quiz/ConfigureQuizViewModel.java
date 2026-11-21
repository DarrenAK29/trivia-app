package interface_adapter.quiz;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Presenter writes to this; the Swing view observes it.
 */
public final class ConfigureQuizViewModel {

    private final PropertyChangeSupport changes =
            new PropertyChangeSupport(this);

    private ConfigureQuizViewState state =
            ConfigureQuizViewState.initial();

    public ConfigureQuizViewState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("state", listener);
    }

    public void setState(ConfigureQuizViewState newState) {
        this.state = Objects.requireNonNull(newState);

        // Always notify the view when the state changes.
        changes.firePropertyChange("state", null, newState);
    }
}