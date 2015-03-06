package pt.aigx.actors.observers;

import pt.aigx.actors.messages.RoutedMessage;

public class RouterObserver extends AbstractObserver {

    @Override
    boolean isToProcess(Object message) {
        RoutedMessage routedMessage = (RoutedMessage) message;
        return routedMessage.isRouted();
    }

    @Override
    void finishExecution() {
        long now = System.currentTimeMillis();
        System.out.println("All messages routed in " + (now - startedTime) + " ms");
    }
}
