package pt.aigx.routing.setups;

import pt.aigx.routing.BaseSetup;
import pt.aigx.routing.routers.RouterActor;

public class RegularRouterSetup extends BaseSetup {

    @Override
    public Class getRouterClass() {
        return RouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "routing";
    }
}