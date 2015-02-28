package pt.aigx.actors;

import akka.actor.UntypedActor;
import pt.aigx.actors.messages.SimpleStringMessage;

/**
 * An abstract Master actor. All actors on this namespace extend from this one. Each child defines which message is for
 * him, and, by default, just publishes a "Done" message to the main event stream if the message is indeed for him.
 * <p>
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

    /**
     * Publishes a "Done" SimpleStringMessage to the main event stream.
     */
    private void publishMessageProcessed() {
        getContext().system().eventStream().publish(new SimpleStringMessage("Done"));
    }

    /**
     * Extracts the text from the SimpleStringMessage object.
     *
     * @param message The message received by the actor
     * @return The extracted text (String)
     */
    protected String extractTextFromMessage(Object message) {
        return ((SimpleStringMessage) message).getText();
    }
}