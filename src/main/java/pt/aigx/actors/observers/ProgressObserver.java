package pt.aigx.actors.observers;

import pt.aigx.Config;
import pt.aigx.Observable;
import pt.aigx.Observer;
import pt.aigx.actors.ObservableActor;

/**
 * Created by emiranda on 23-02-2015.
 */
public class ProgressObserver implements Observer {

    long startedTime = 0;

    ObservableActor actor = null;

    @Override
    public void update(Observable observable) {

        this.startTimer();
        ObservableActor actor = (ObservableActor) observable;

        if (this.areAllMessagesProcessed(actor)) {
            this.finishExecution(actor);
        }

        // TODO: Print each 25% of the progress
    }

    private void startTimer() {
        if (this.startedTime != 0) {
            this.startedTime = System.currentTimeMillis();
        }
    }

    /**
     * @return True if all messages are processed, otherwise, false.
     */
    private boolean areAllMessagesProcessed(ObservableActor actor) {
        return (actor.getProcessedMessages() == Config.NUMBER_OF_MESSAGES);
    }

    /**
     *
     */
    private void finishExecution(ObservableActor actor) {

        long now = System.currentTimeMillis();

        System.out.println("All messages processed in " + (now - startedTime) / 1000 + " seconds");
        System.out.println("Total Controller of messages processed " + actor.getProcessedMessages());
        actor.getContext().system().shutdown();
    }
}
