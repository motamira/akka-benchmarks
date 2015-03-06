package pt.aigx.actors.messages;

import java.io.Serializable;

public class RoutedMessage implements Serializable {

    private boolean isRouted;

    public RoutedMessage(boolean isRouted) {
        this.isRouted = isRouted;
    }

    public boolean isRouted() {
        return isRouted;
    }
}
