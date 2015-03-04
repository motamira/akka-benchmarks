package pt.aigx;

import akka.actor.ActorRef;
import pt.aigx.actors.messages.RouterStringMessage;
import java.util.Map;
import java.util.Random;

public class RandomMessageSender {

    private final static Random random = new Random();

    /**
     * Method that generates load (sends messages to the router actor). Messages will be later sent to each classifier
     * randomly.
     *
     * @see pt.aigx.Config.NUMBER_OF_MESSAGES
     */
    public void send(ActorRef routerActor, Map<String, ActorRef> availableActors) {

        long startedTime = System.currentTimeMillis();
        System.out.println("Starting feeding router with messages");

        for (int i = Config.NUMBER_OF_MESSAGES; i > 0; i--) {
            int randomIndex = this.getRandomActorIndex();
            String actorName = Config.actors[randomIndex];
            routerActor.tell(new RouterStringMessage(availableActors.get(actorName), actorName), null);
        }

        long now = System.currentTimeMillis();
        System.out.println("All jobs sent successfully in " + (now - startedTime) / 1000 + " seconds");
    }

    /**
     * @return The generated actor index to be used in the message sending
     */
    private int getRandomActorIndex() {
        return random.nextInt(Config.actors.length);
    }
}
