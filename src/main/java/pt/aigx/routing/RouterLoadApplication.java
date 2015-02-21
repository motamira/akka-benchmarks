package pt.aigx.routing;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;
import pt.aigx.Config;
import pt.aigx.actors.ControllerActor;
import pt.aigx.actors.RouterActor;
import pt.aigx.actors.messages.SimpleStringMessage;


public class RouterLoadApplication {

    private ActorSystem system;
    private ActorRef routerActor;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new RouterLoadApplication().generateLoad();
    }

    public RouterLoadApplication() {
        system = ActorSystem.create("RouterSystem", ConfigFactory.load("router"));
        this.setupActors();
    }

    private void setupActors() {

        Props controllerActorProps = Props.create(ControllerActor.class);
        system.actorOf(controllerActorProps, "controllerActor");

        Props routerProps = FromConfig.getInstance().props(Props.create(RouterActor.class));
        this.routerActor = system.actorOf(routerProps, "routerActor");
    }

    private void generateLoad() {

        long startedTime = System.currentTimeMillis();
        System.out.println("Starting feeding router with messages");

        for (int i = Config.NUMBER_OF_MESSAGES; i > 0; i--) {
            String message = "Job Id " + i + "# send";
            routerActor.tell(new SimpleStringMessage(message), null);
//            controllerActor.tell(new SimpleStringMessage("Done"), null);
        }

        long now = System.currentTimeMillis();
        System.out.println("All jobs sent successfully in " + (now - startedTime) / 1000 + " seconds");
    }
}