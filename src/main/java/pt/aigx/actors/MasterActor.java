package pt.aigx.actors;

import akka.actor.UntypedActor;
import pt.aigx.actors.messages.SimpleStringMessage;

/**
 * Created by emiranda on 23-02-2015.
 */
abstract public class MasterActor extends UntypedActor {

    /**
     * @param text The received message as text
     * @return Whether the message is or is not for the current actor.
     */
    abstract public boolean isMessageForMe(String text);

    @Override
    public void onReceive(Object message) throws Exception {

        String text = this.extractTextFromMessage(message);

        if (this.isMessageForMe(text)) {
            // In a real system some kind of process would be executed here
            this.publishMessageProcessed();
        }
    }

    private void publishMessageProcessed() {
        getContext().system().eventStream().publish(new SimpleStringMessage("Done"));
    }

    protected String extractTextFromMessage(Object message) {
        return ((SimpleStringMessage) message).getText();
    }
}