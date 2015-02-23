package pt.aigx.actors;

import akka.actor.UntypedActor;
import pt.aigx.Observable;
import pt.aigx.Observer;
import pt.aigx.actors.messages.SimpleStringMessage;

/**
 * Created by emiranda on 23-02-2015.
 */
abstract public class ObservableActor extends UntypedActor implements Observable {

    private Observer progressObserver = null;

    int processedMessages = 0;

    abstract public boolean isMessageForMe(String text);

    public int getProcessedMessages() {
        return processedMessages;
    }

    public void processMessage() {
        this.processedMessages++;
    }

    @Override
    public void attach(Observer observer) {
        this.progressObserver = observer;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        String text = this.extractTextFromMessage(message);

        if (this.isMessageForMe(text)) {
            this.processMessage();
            this.notifyChange();
        }
    }

    private void notifyChange() {
        if (this.progressObserver != null) {
            this.progressObserver.update(this);
        }
    }

    protected String extractTextFromMessage(Object message) {
        return ((SimpleStringMessage) message).getText();
    }
}