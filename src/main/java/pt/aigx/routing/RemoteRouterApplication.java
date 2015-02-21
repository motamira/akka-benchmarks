package pt.aigx.routing;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.typesafe.config.ConfigFactory;
import pt.aigx.actors.ControllerActor;
import pt.aigx.actors.RouterActor;

/**
 * Created by emiranda on 2/20/15.
 */
public class RemoteRouterApplication {

    public static void main(String args[]) {
        startRemoteRouterSystem();
    }

    public static void startRemoteRouterSystem() {

        final ActorSystem system = ActorSystem.create("RouterSystem", ConfigFactory.load("remote-router"));

        Props controllerActorProps = Props.create(ControllerActor.class);
        system.actorOf(controllerActorProps, "controllerActor");

        Props routerProps = FromConfig.getInstance().props(Props.create(RouterActor.class));
        system.actorOf(routerProps, "routerActor");
    }
}