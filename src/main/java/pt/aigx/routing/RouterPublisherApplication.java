package pt.aigx.routing;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.FromConfig;
import pt.aigx.actors.messages.SimpleStringMessage;
import pt.aigx.routing.actors.PublisherRouterActor;

/**
 * Created by emiranda on 2/27/15.
 */
public class RouterPublisherApplication extends RouterApplication {

    public static void main(String[] args) {
        new RouterPublisherApplication().generateLoad();
    }

    public RouterPublisherApplication() {
        super();
        for (ActorRef actor : this.getActorReferences()) {
            getSystem().eventStream().subscribe(actor, SimpleStringMessage.class);
        }
    }

    protected void setupRouterActor() {
        Props routerProps = FromConfig.getInstance().props(Props.create(PublisherRouterActor.class));
        this.setRouterActor(this.getSystem().actorOf(routerProps, "routerActor"));
    }
}
