package pt.aigx.routing;

import pt.aigx.RandomMessageSender;
import pt.aigx.routing.setups.RegularRouterSetup;

public class RouterApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        RegularRouterSetup setup = new RegularRouterSetup();
        new RandomMessageSender().send(setup.getRouterActor(), setup.getActorReferences());
    }
}