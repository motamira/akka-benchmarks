package pt.aigx.routing;

import pt.aigx.RandomMessageSender;
import pt.aigx.routing.setups.PublisherRouterSetup;

public class RouterPublisherApplication {

    public static void main(String[] args) {
        PublisherRouterSetup setup = new PublisherRouterSetup();
        new RandomMessageSender().send(setup.getRouterActor(), setup.getActorReferences());
    }
}
