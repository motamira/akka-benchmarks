package pt.aigx;

public class Config {

    public final static int NUMBER_OF_MESSAGES = 1 * 1000000;

    // Global Actors
    public final static String routerActor = "routerActor";
    public final static String[] actors = new String[]{"Pitt", "Spacey", "Clooney"};

    // Remoting
    public final static String remoteActorsBasePath = "akka.tcp://RouterSystem@127.0.0.1:2555/user";
    public final static String remoteRouterBasePath = "akka.tcp://RouterSystem@127.0.0.1:2555/user";
}