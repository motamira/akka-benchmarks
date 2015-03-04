package pt.aigx;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;
import pt.aigx.actors.ProgressObserver;
import pt.aigx.actors.messages.SimpleStringMessage;
import java.util.HashMap;
import java.util.Map;

abstract public class BaseSetup {

    private final static String actorsNamespace = "pt.aigx.actors";

    private ActorSystem system;
    private Map<String, ActorRef> actorReferences = new HashMap<>();
    private ActorRef routerActor;

    abstract public Class getRouterClass();

    abstract public String getSystemConfig();

    public Map<String, ActorRef> getActorReferences() {
        return actorReferences;
    }

    public ActorSystem getSystem() {
        return system;
    }

    public ActorRef getRouterActor() {
        return routerActor;
    }

    public void setRouterActor(ActorRef routerActor) {
        this.routerActor = routerActor;
    }

    public BaseSetup() {
        this.setup();
    }

    private void setup() {
        try {
            this.setupSystem();
            this.setupActors();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupSystem() {
        system = ActorSystem.create("RouterSystem", ConfigFactory.load(this.getSystemConfig()));
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
     * Setups as many actors as defined in the {@link Config.actors} array. These behave as "classifiers" since they publish
     * a message as soon as they are done with processing something.
     */
    private void setupClassifierActors() throws ClassNotFoundException {
        for (String actor : Config.actors) {
            Props actorProps = Props.create(Class.forName(String.format("%s.%s", actorsNamespace, actor)));
            this.actorReferences.put(actor, system.actorOf(actorProps, actor));
        }
    }

    /**
     * Setups the router actor using the resources config. This router actor only redirects the message to one of the
     * classifier actors. Since we can define the number of routees, the router is mainly to increase / decrease the
     * message throughput.
     */
    private void setupRouterActor() {
        Props routerProps = FromConfig.getInstance().props(Props.create(this.getRouterClass()));
        this.setRouterActor(system.actorOf(routerProps, "routerActor"));
    }
}
