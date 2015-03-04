package pt.aigx.remoting.actors;

import akka.actor.*;
import pt.aigx.Config;
import pt.aigx.RandomMessageSender;
import pt.aigx.routing.actors.RouterActor;

import java.util.HashMap;
import java.util.Map;

public class RemoteLookupActor extends UntypedActor {

    private final static String remoteBasePath = "akka.tcp://RouterSystem@127.0.0.1:2555/user";

    private Map<String, ActorRef> actorReferences = new HashMap<>();
    private int identifiedActors = 0;

    public RemoteLookupActor() {
        this.discoverActors();
    }

    private void discoverActors() {
        for (String actor : Config.actors) {
            String fullPath = String.format("%s/%s", remoteBasePath, actor);
            getContext().actorSelection(fullPath).tell(new Identify(fullPath), getSelf());
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (this.isMessageActorIdentity(message)) {
            ActorRef actor = ((ActorIdentity) message).getRef();

            this.actorReferences.put(actor.path().name(), actor);
            identifiedActors++;

            if (Config.actors.length == identifiedActors) {
                ActorRef router = getContext().system().actorOf(Props.create(RouterActor.class), "remoteRouterActor");
                new RandomMessageSender().send(router, this.actorReferences);
            }
        }
    }

    /**
     * @param message
     * @return
     */
    private boolean isMessageActorIdentity(Object message) {
        return (message instanceof ActorIdentity);
    }
}
