package pt.aigx.routing;

import pt.aigx.routing.setups.PublisherRouterSetup;

public class RouterPublisherApplication {

    public static void main(String[] args) {
        PublisherRouterSetup setup = new PublisherRouterSetup();
        new RouterPumper(setup.getRouterActor(), setup.getActorReferences()).pump();
    }
}
