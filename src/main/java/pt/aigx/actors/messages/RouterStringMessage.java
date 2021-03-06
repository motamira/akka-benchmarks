package pt.aigx.actors.messages;

import akka.actor.ActorRef;
import java.io.Serializable;

public class RouterStringMessage implements Serializable {

    private final ActorRef destinationActorRef;
    private final String actorName;

    public RouterStringMessage(ActorRef destinationActor, String actorName) {
        this.destinationActorRef = destinationActor;
        this.actorName = actorName;
    }

    public ActorRef getDestinationActorRef() {
        return destinationActorRef;
    }

    public String getActorName() {
        return actorName;
    }
}
