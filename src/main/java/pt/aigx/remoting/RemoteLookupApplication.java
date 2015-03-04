package pt.aigx.remoting;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import pt.aigx.remoting.actors.RemoteLookupActor;

public class RemoteLookupApplication {

    public static void main(String args[]) {
        new RemoteLookupApplication().startRemoteLookupSystem();
    }

    public void startRemoteLookupSystem() {
        final ActorSystem system = ActorSystem.create("RemoteLookupSystem", ConfigFactory.load("remote-lookup"));
        system.actorOf(Props.create(RemoteLookupActor.class), "remoteLookupActor");
    }
}