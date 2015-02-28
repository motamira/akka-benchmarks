package pt.aigx.routing;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;
import pt.aigx.Config;
import pt.aigx.actors.ProgressObserver;
import pt.aigx.actors.messages.RouterStringMessage;
import pt.aigx.routing.actors.RouterActor;
import pt.aigx.actors.messages.SimpleStringMessage;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class RouterApplication {

    private final static String actorsNamespace = "pt.aigx.actors";
    private final static Random random = new Random();

    private ActorSystem system;
    private ArrayList<ActorRef> actorReferences = new ArrayList<>(Config.actors.length - 1);
    private ActorRef routerActor;

    public ArrayList<ActorRef> getActorReferences() {
        return actorReferences;
    }

    public ActorSystem getSystem() {
        return system;
    }

    public void setRouterActor(ActorRef routerActor) {
        this.routerActor = routerActor;
    }

    public ActorRef getRouterActor() {
        return routerActor;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new RouterApplication().generateLoad();
    }

    public RouterApplication() {
        try {
            this.setupSystem();
            this.setupActors();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupSystem() {
        system = ActorSystem.create("RouterSystem", ConfigFactory.load("router"));
    }

    /**
     * Setups the system actors. There are three different actor roles here: Subscribers, Classifiers and Router.
     * The first type subscribes itself to the main system event bus, the second type publishes messages to the
     * main event bus and the third routes messages between the three classifiers.
     */
    private void setupActors() throws ClassNotFoundException {
        this.setupSubscriberActors();
        this.setupClassifierActors();
        this.setupRouterActor();
    }

    /**
     * Subscribes the ProgressObserver actor to the event stream, specifically to Simple String messages.
     *
     * @see pt.aigx.actors.ProgressObserver
     * @see pt.aigx.actors.messages.SimpleStringMessage
     */
    private void setupSubscriberActors() {
        Props actorProps = Props.create(ProgressObserver.class);
        ActorRef progressObserver = system.actorOf(actorProps, "ProgressObserverActor");
        system.eventStream().subscribe(progressObserver, SimpleStringMessage.class);
    }

    /**
     * Setups as many actors as defined in the {@link actors} array. These behave as "classifiers" since they publish
     * a message as soon as they are done with processing something.
     */
    private void setupClassifierActors() throws ClassNotFoundException {

        this.actorReferences = new ArrayList<>();

        for (String actor : Config.actors) {
            Props actorProps = Props.create(Class.forName(String.format("%s.%s", actorsNamespace, actor)));
            this.actorReferences.add(system.actorOf(actorProps, actor));
        }
    }

    /**
     * Setups the router actor using the resources config. This router actor only redirects the message to one of the
     * classifier actors. Since we can define the number of routees, the router is mainly to increase / decrease the
     * message throughput.
     */
    protected void setupRouterActor() {
        Props routerProps = FromConfig.getInstance().props(Props.create(RouterActor.class));
        this.setRouterActor(system.actorOf(routerProps, "routerActor"));
    }

    /**
     * Method that generates load (sends messages to the router actor). Messages will be later sent to each classifier
     * randomly.
     *
     * @see pt.aigx.Config.NUMBER_OF_MESSAGES
     */
    protected void generateLoad() {

        long startedTime = System.currentTimeMillis();
        System.out.println("Starting feeding router with messages");

        for (int i = Config.NUMBER_OF_MESSAGES; i > 0; i--) {
            int randomIndex = this.getRandomActorIndex();
            // To send messages through the router actor use the line below
            this.getRouterActor().tell(
                    new RouterStringMessage(this.actorReferences.get(randomIndex), Config.actors[randomIndex]), null);
            // To bypass the router use the line below
//          this.actorReferences.get(randomIndex).tell(new SimpleStringMessage(Config.actors[randomIndex]), null);
        }

        long now = System.currentTimeMillis();
        System.out.println("All jobs sent successfully in " + (now - startedTime) / 1000 + " seconds");
    }

    /**
     * @return The generated actor index to be used in the message sending
     */
    private int getRandomActorIndex() {
        return random.nextInt(Config.actors.length);
    }
}