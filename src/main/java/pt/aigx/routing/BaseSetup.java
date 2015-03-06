package pt.aigx.routing;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;
import pt.aigx.Config;
import pt.aigx.actors.messages.RoutedMessage;
import pt.aigx.actors.observers.ProgressObserver;
import pt.aigx.actors.messages.SimpleStringMessage;
import pt.aigx.actors.observers.RouterObserver;
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
     * @throws ClassNotFoundException
     */
    private void setupActors() throws ClassNotFoundException {
        this.setupObservers();
        this.setupEndActors();
        this.setupRouterActor();
    }

    /**
     * @see pt.aigx.actors.observers.ProgressObserver
     * @see pt.aigx.actors.messages.SimpleStringMessage
     */
    private void setupObservers() {
        Props actorProps = Props.create(ProgressObserver.class);
        ActorRef progressObserver = system.actorOf(actorProps, "ProgressObserverActor");
        system.eventStream().subscribe(progressObserver, SimpleStringMessage.class);

        Props routerObsProps = Props.create(RouterObserver.class);
        ActorRef routerObserver = system.actorOf(routerObsProps, "RouterObserverActor");
        system.eventStream().subscribe(routerObserver, RoutedMessage.class);
    }

    /**
     * Setups as many actors as defined in the {@link pt.aigx.Config.actors} array. These just publish a message as soon
     * as they are done with processing something (Of course nothing real is processed for testing purposes).
     */
    private void setupEndActors() throws ClassNotFoundException {
        for (String actor : Config.actors) {
            Props actorProps = FromConfig.getInstance().props(
                    Props.create(Class.forName(String.format("%s.%s", actorsNamespace, actor))));
            this.actorReferences.put(actor, system.actorOf(actorProps, actor));
        }
    }

    /**
     * Setups the router actor using the resources config. This router actor only redirects the message to one of the
     * end actors. Since we can define the number of routees, the router is mainly to increase / decrease the
     * message throughput.
     */
    private void setupRouterActor() {
        Props routerProps = FromConfig.getInstance().props(Props.create(this.getRouterClass()));
        this.setRouterActor(system.actorOf(routerProps, Config.routerActor));
    }
}