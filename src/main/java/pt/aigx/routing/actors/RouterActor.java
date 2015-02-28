package pt.aigx.routing.actors;

import akka.actor.UntypedActor;
import pt.aigx.actors.messages.RouterStringMessage;
import pt.aigx.actors.messages.SimpleStringMessage;

public class RouterActor extends UntypedActor {

    public RouterActor() {
        System.out.println("Router actor created");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        RouterStringMessage routerMessage = (RouterStringMessage) message;
        routerMessage.getDestinationActorRef().tell(new SimpleStringMessage(routerMessage.getActorName()), getSender());
    }
}