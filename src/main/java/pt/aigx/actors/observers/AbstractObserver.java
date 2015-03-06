package pt.aigx.actors.observers;

import akka.actor.UntypedActor;
import pt.aigx.Config;

abstract public class AbstractObserver extends UntypedActor {

    long startedTime = 0;
    int processedMessages = 0;

    abstract boolean isToProcess(Object message);
    abstract void finishExecution();

    /**
     * @param message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {

        this.startTimer();

        if (this.isToProcess(message)) {

            this.messageProcessed();

            if (this.isToPrintProgress()) {
                this.printProgress();
            }

            if (this.areAllMessagesProcessed()) {
                this.finishExecution();
            }
        }
    }

    private void messageProcessed() {
        this.processedMessages++;
    }

    private boolean isToPrintProgress() {
        return ((this.processedMessages % (Config.NUMBER_OF_MESSAGES / 4)) == 0);
    }

    private void printProgress() {
        System.out.println(this.getClass().getSimpleName() + ": Processed " + processedMessages + " messages");
    }

    private boolean areAllMessagesProcessed() {
        return (this.processedMessages == Config.NUMBER_OF_MESSAGES);
    }

    private void startTimer() {
        if (this.startedTime == 0) {
            System.out.println(this.getClass().getSimpleName() + ": Timer started");
            this.startedTime = System.currentTimeMillis();
        }
    }
}
