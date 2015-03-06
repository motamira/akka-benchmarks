package pt.aigx.routing.routers;

import akka.actor.UntypedActor;
import pt.aigx.actors.messages.RouterStringMessage;
import pt.aigx.actors.messages.SimpleStringMessage;

public class PublisherRouterActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        RouterStringMessage routerMessage = (RouterStringMessage) message;
        getContext().system().eventStream().publish(new SimpleStringMessage(routerMessage.getActorName()));
    }
}
