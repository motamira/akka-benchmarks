package pt.aigx.actors;

import pt.aigx.Config;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by emiranda on 23-02-2015.
 */
public class ProgressObserver extends MasterActor {

    long startedTime = 0;
    int processedMessages = 0;


    /**
     * @param message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {

        this.startTimer();

        if (this.isMessageForMe(this.extractTextFromMessage(message))) {
            this.messageProcessed();

            if (this.isToPrintProgress()) {
                this.printProgress();
            }

            if (this.areAllMessagesProcessed()) {
                this.finishExecution();
            }
        }
    }

    private void startTimer() {
        if (this.startedTime == 0) {
            System.out.println("Processing timer started");
            this.startedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Done") == 0);
    }

    private void messageProcessed() {
        this.processedMessages++;
    }

    private boolean isToPrintProgress() {
        return ((this.processedMessages % (Config.NUMBER_OF_MESSAGES / 4)) == 0);
    }

    private void printProgress() {
        System.out.println("Processed " + processedMessages + " messages");
    }

    /**
     * @return True if all messages are processed, otherwise, false.
     */
    private boolean areAllMessagesProcessed() {
        return (this.processedMessages == Config.NUMBER_OF_MESSAGES);
    }

    /**
     * Terminates the program. This is called when all messages are processed.
     */
    private void finishExecution() {

        long now = System.currentTimeMillis();

        System.out.println("All messages processed in " + (now - startedTime) / 1000 + " seconds");
        System.out.println("Total Controller of messages processed " + this.processedMessages);
        getContext().system().scheduler().scheduleOnce(Duration.create(5, TimeUnit.SECONDS), new Runnable() {
            @Override
            public void run() {
                System.out.println("System shutting down");
                getContext().system().shutdown();
            }
        }, getContext().system().dispatcher());
    }
}
