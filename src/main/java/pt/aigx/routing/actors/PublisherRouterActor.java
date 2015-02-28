package pt.aigx.routing.actors;

import akka.actor.UntypedActor;
import pt.aigx.actors.messages.RouterStringMessage;
import pt.aigx.actors.messages.SimpleStringMessage;

/**
 * Created by emiranda on 2/27/15.
 */
public class PublisherRouterActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        RouterStringMessage routerMessage = (RouterStringMessage) message;
        getContext().system().eventStream().publish(new SimpleStringMessage(routerMessage.getActorName()));
    }
}
