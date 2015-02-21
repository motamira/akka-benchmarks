package pt.aigx.remoting;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import pt.aigx.remoting.actors.RemoteLookupActor;

/**
 * Created by emiranda on 2/20/15.
 */
public class RemoteLookupApplication {

    private final static String remotePath = "akka.tcp://RouterSystem@127.0.0.1:2555/user/routerActor";

    public static void main(String args[]) {
        startRemoteLookupSystem();
    }

    public static void startRemoteLookupSystem() {

        final ActorSystem system = ActorSystem.create("RemoteLookupSystem", ConfigFactory.load("remote-lookup"));
        system.actorOf(Props.create(RemoteLookupActor.class, remotePath), "remoteLookupActor");

        System.out.println("Started Remote Lookup System");
    }
}
