package pt.aigx.routing.remoting.setups;

import pt.aigx.routing.BaseSetup;
import pt.aigx.routing.routers.RouterActor;

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