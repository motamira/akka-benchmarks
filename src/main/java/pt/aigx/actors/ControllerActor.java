package pt.aigx.actors;

import akka.actor.UntypedActor;
import pt.aigx.Config;
import pt.aigx.actors.messages.SimpleStringMessage;

public class ControllerActor extends UntypedActor {

    int processedMessages = 0;
    long startedTime = System.currentTimeMillis();

    /**
     * @param message Message received by the WorkerActor.
     * @throws Exception
     */
    public void onReceive(Object message) throws Exception {
        if (this.isSimpleString(message)) {
            this.process(message);
        }
    }

    /**
     * @param message Message received by the WorkerActor.
     * @return True if the message is an instance if WorkerMessage, otherwise, false.
     */
    private boolean isSimpleString(Object message) {
        return (message instanceof SimpleStringMessage);
    }

    /**
     * @param message Message received by the WorkerActor.
     */
    private void process(Object message) {

        String text = ((SimpleStringMessage) message).getText();

        if (this.messageEqualsDone(text)) {

            processedMessages++;

            if (this.areAllMessagesProcessed()) {
                this.finishExecution();
            }
        }
    }

    /**
     * @param text A string to compare to "Done"
     * @return True if the passed string equals to "Done". False otherwise.
     */
    private boolean messageEqualsDone(String text) {
        return (text.compareTo("Done") == 0);
    }

    /**
     * @return True if all messages are processed, otherwise, false.
     */
    private boolean areAllMessagesProcessed() {
        return (processedMessages == Config.NUMBER_OF_MESSAGES);
    }

    /**
     *
     */
    private void finishExecution() {

        long now = System.currentTimeMillis();

        System.out.println("All messages processed in " + (now - startedTime) / 1000 + " seconds");
        System.out.println("Total Controller of messages processed " + processedMessages);
        getContext().system().shutdown();
    }
}