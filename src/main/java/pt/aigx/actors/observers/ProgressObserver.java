package pt.aigx.actors.observers;

import pt.aigx.actors.messages.SimpleStringMessage;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

public class ProgressObserver extends AbstractObserver {

    @Override
    boolean isToProcess(Object message) {
        SimpleStringMessage simpleMessage = (SimpleStringMessage) message;
        return simpleMessage.getText().equals("Done");
    }

    /**
     * Terminates the program. This is called when all messages are processed.
     */
    public void finishExecution() {

        long now = System.currentTimeMillis();

        System.out.println("All messages processed in " + (now - startedTime) + " ms");
        System.out.println("Total messages processed " + this.processedMessages);
        getContext().system().scheduler().scheduleOnce(Duration.create(5, TimeUnit.SECONDS), new Runnable() {
            @Override
            public void run() {
                System.out.println("System shutting down");
                getContext().system().shutdown();
            }
        }, getContext().system().dispatcher());
    }
}
