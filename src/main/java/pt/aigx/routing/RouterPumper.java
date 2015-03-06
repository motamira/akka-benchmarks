package pt.aigx.routing;

import akka.actor.ActorRef;
import pt.aigx.Config;
import pt.aigx.actors.messages.RouterStringMessage;
import java.util.Map;
import java.util.Random;

/**
 * Class that generates load, i.e., pumps messages to the given router actor. Messages will be later routed to each
 * end actor randomly.
 */
public class RouterPumper {

    private final static Random random = new Random();

    private ActorRef routerActor;
    private Map<String, ActorRef> endActors;

    public RouterPumper(ActorRef routerActor, Map<String, ActorRef> endActors) {
        this.routerActor = routerActor;
        this.endActors = endActors;
    }

    /**
     * @see pt.aigx.Config.NUMBER_OF_MESSAGES
     */
    public void pump() {

        long startedTime = System.currentTimeMillis();
        System.out.println("Starting feeding router with messages");

        for (int i = Config.NUMBER_OF_MESSAGES; i > 0; i--) {
            int randomIndex = this.getRandomActorIndex();
            String actorName = Config.actors[randomIndex];
            routerActor.tell(new RouterStringMessage(endActors.get(actorName), actorName), null);
        }

        long now = System.currentTimeMillis();
        System.out.println("All jobs sent successfully in " + (now - startedTime) + " ms");
    }

    /**
     * @return The generated actor index to be used in the message sending
     */
    private int getRandomActorIndex() {
        return random.nextInt(endActors.size());
    }
}
