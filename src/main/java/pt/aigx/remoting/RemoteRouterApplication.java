package pt.aigx.remoting;

import pt.aigx.AbstractRouterApplication;
import pt.aigx.routing.actors.RouterActor;

public class RemoteRouterApplication extends AbstractRouterApplication {

    public static void main(String args[]) {
        new RemoteRouterApplication();
    }

    @Override
    public Class getRouterClass() {
        return RouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "remote-router";
    }
}