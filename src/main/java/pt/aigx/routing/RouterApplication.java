package pt.aigx.routing;

import pt.aigx.routing.setups.RegularRouterSetup;

public class RouterApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        RegularRouterSetup setup = new RegularRouterSetup();
        new RouterPumper(setup.getRouterActor(), setup.getActorReferences()).pump();
    }
}