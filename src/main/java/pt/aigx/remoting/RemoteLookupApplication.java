package pt.aigx.remoting;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import pt.aigx.remoting.actors.RemoteLookupActor;

public class RemoteLookupApplication {

    private final static String remotePath = "akka.tcp://RouterSystem@192.168.1.5:2555/user/controllerActor";

    public static void main(String args[]) {
        startRemoteLookupSystem();
    }

    public static void startRemoteLookupSystem() {

        final ActorSystem system = ActorSystem.create("RemoteLookupSystem", ConfigFactory.load("remote-lookup"));
        system.actorOf(Props.create(RemoteLookupActor.class, remotePath), "remoteLookupActor");

        System.out.println("Started Remote Lookup System");
    }
}
