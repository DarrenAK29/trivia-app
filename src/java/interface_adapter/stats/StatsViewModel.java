package interface_adapter.stats;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public final class StatsViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private StatsViewState state = StatsViewState.empty();

    public StatsViewState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("state", listener);
    }

    public void setState(StatsViewState newState) {
        this.state = Objects.requireNonNull(newState);
        changes.firePropertyChange("state", null, newState);
    }
}
