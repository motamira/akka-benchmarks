package pt.aigx.routing.setups;

import akka.actor.ActorRef;
import pt.aigx.routing.BaseSetup;
import pt.aigx.actors.messages.SimpleStringMessage;
import pt.aigx.routing.routers.PublisherRouterActor;
import java.util.Map;

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
        return "routing";
    }
}