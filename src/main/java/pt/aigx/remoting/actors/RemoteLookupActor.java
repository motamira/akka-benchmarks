package pt.aigx.remoting.actors;

import akka.actor.*;
import pt.aigx.Config;
import pt.aigx.actors.messages.SimpleStringMessage;

/**
 * Created by emiranda on 2/20/15.
 */
public class RemoteLookupActor extends UntypedActor {

    private final String remotePath;
    private ActorRef remoteRouterActor = null;

    public RemoteLookupActor(String remotePath) {
        this.remotePath = remotePath;
        this.sendIdentifyRequest();
    }

    private void sendIdentifyRequest() {
        getContext().actorSelection(remotePath).tell(new Identify(remotePath), getSelf());
//        getContext().system().scheduler()
//                .scheduleOnce(Duration.create(1, TimeUnit.SECONDS), getSelf(),
//                        ReceiveTimeout.getInstance(), getContext().dispatcher(), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (this.isMessageActorIdentity(message)) {

            remoteRouterActor = ((ActorIdentity) message).getRef();

            if (!this.isRemoteActorAvailable()) {
                System.out.println("Remote actor not available: " + remotePath);
                this.sendIdentifyRequest();
            } else {
                this.feedRequests();
            }
        }
    }

    private void feedRequests() {

        long startedTime = System.currentTimeMillis();
        System.out.println("Starting feeding remote actor with messages");

        for (int i = Config.NUMBER_OF_MESSAGES; i > 0; i--) {
            remoteRouterActor.tell(new SimpleStringMessage("Done"), getSelf());
        }

        long now = System.currentTimeMillis();
        System.out.println("All jobs sent successfully in " + (now - startedTime) / 1000 + " seconds");
    }

    /**
     * @param message
     * @return
     */
    private boolean isMessageActorIdentity(Object message) {
        return (message instanceof ActorIdentity);
    }

    /**
     * @return Whether the remote controller actor is available or not.
     */
    private boolean isRemoteActorAvailable() {
        return (remoteRouterActor != null);
    }
}
