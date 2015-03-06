package pt.aigx.routing.remoting.actors;

import akka.actor.*;
import pt.aigx.Config;
import pt.aigx.routing.RouterPumper;
import java.util.HashMap;
import java.util.Map;

public class RemoteLookupActor extends UntypedActor {

    private Map<String, ActorRef> actorReferences = new HashMap<>();
    private ActorRef routerActor;
    private int identifiedActors = 0;

    public RemoteLookupActor() {
        this.discoverActors();
    }

    private void discoverActors() {
        this.discoverEndActors();
        this.discoverRemoteRouter();
    }

    private void discoverEndActors() {
        for (String actor : Config.actors) {
            String fullPath = String.format("%s/%s", Config.remoteActorsBasePath, actor);
            getContext().actorSelection(fullPath).tell(new Identify(fullPath), getSelf());
        }
    }

    private void discoverRemoteRouter() {
        String fullPath = String.format("%s/%s", Config.remoteRouterBasePath, Config.routerActor);
        getContext().actorSelection(fullPath).tell(new Identify(fullPath), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (this.isMessageActorIdentity(message)) {

            ActorRef actor = ((ActorIdentity) message).getRef();
            String actorName = actor.path().name();

            if (this.isRouter(actorName)) {
                this.routerActor = actor;
            } else {
                this.actorReferences.put(actorName, actor);
                identifiedActors++;
            }
        }

        if (Config.actors.length == identifiedActors && this.routerActor != null) {
            new RouterPumper(this.routerActor, this.actorReferences).pump();
        }
    }

    private boolean isMessageActorIdentity(Object message) {
        return (message instanceof ActorIdentity);
    }

    private boolean isRouter(String actorName) {return actorName.equals(Config.routerActor);}
}