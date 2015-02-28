package pt.aigx.routing;

import pt.aigx.AbstractRouterApplication;
import pt.aigx.routing.actors.RouterActor;

public class RouterApplication extends AbstractRouterApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new RouterApplication().generateLoad();
    }

    @Override
    public Class getRouterClass() {
        return RouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "router";
    }
}