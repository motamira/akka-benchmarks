package pt.aigx.remoting.setups;

import pt.aigx.BaseSetup;
import pt.aigx.routing.actors.RouterActor;

public class RemoteRouterSetup extends BaseSetup {

    @Override
    public Class getRouterClass() {
        return RouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "remote-router";
    }
}