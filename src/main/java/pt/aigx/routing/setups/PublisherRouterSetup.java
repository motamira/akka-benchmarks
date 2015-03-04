package pt.aigx.routing.setups;

import akka.actor.ActorRef;
import pt.aigx.BaseSetup;
import pt.aigx.actors.messages.SimpleStringMessage;
import pt.aigx.routing.actors.PublisherRouterActor;

import java.util.Map;

/**
 * Created by emiranda on 28-02-2015.
 */
public class PublisherRouterSetup extends BaseSetup {

    public PublisherRouterSetup() {
        super();
        for (Map.Entry<String, ActorRef> entry : this.getActorReferences().entrySet()) {
            getSystem().eventStream().subscribe(entry.getValue(), SimpleStringMessage.class);
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