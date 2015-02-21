package pt.aigx.actors;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.UntypedActor;
import pt.aigx.actors.messages.SimpleStringMessage;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

public class RouterActor extends UntypedActor {

    private int messageProcessingDelay = 2000;
    private final static String controllerPath = "akka://RouterSystem/user/controllerActor";
    private ActorRef controllerActor = null;

    public RouterActor() {
        System.out.println("Router actor created");
        this.sendIdentifyRequest();
    }

    private void sendIdentifyRequest() {
        getContext().actorSelection(controllerPath).tell(new Identify(controllerPath), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (this.isMessageActorIdentity(message)) {
            this.controllerActor = ((ActorIdentity) message).getRef();
        } else {
            if (this.isControllerActorAvailable()) {
                this.sendMessage();
            } else {
                System.out.println("Controller not available. Retry identify request");
                this.sendIdentifyRequest();
            }
        }
    }

    private boolean isMessageActorIdentity(Object message) {
        return (message instanceof ActorIdentity);
    }

    private boolean isControllerActorAvailable() {
        return (controllerActor != null);
    }

    private void sendMessage() {
        controllerActor.tell(new SimpleStringMessage("Done"), getSelf());
    }

    private void sendMessageWithDelay() {
        getContext().system().scheduler().scheduleOnce(
                Duration.create(messageProcessingDelay, TimeUnit.MILLISECONDS),
                controllerActor, new SimpleStringMessage("Done"), getContext().dispatcher(), getSelf());
    }
}