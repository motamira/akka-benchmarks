package pt.aigx.routing.setups;

import pt.aigx.BaseSetup;
import pt.aigx.routing.actors.RouterActor;

/**
 * Created by emiranda on 28-02-2015.
 */
public class RegularRouterSetup extends BaseSetup {

    @Override
    public Class getRouterClass() {
        return RouterActor.class;
    }

    @Override
    public String getSystemConfig() {
        return "router";
    }
}