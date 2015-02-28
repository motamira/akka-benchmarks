package pt.aigx.routing;

import akka.actor.ActorRef;
import pt.aigx.AbstractRouterApplication;
import pt.aigx.actors.messages.SimpleStringMessage;
import pt.aigx.routing.actors.PublisherRouterActor;

/**
 * Created by emiranda on 2/27/15.
 */
public class RouterPublisherApplication extends AbstractRouterApplication {

    public static void main(String[] args) {
        new RouterPublisherApplication().generateLoad();
    }

    public RouterPublisherApplication() {
        super();
        for (ActorRef actor : this.getActorReferences()) {
            getSystem().eventStream().subscribe(actor, SimpleStringMessage.class);
        }
    }

    @Override
    public Class getRouterClass() {
        return PublisherRouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "router";
    }
}
