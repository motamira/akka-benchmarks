package pt.aigx.actors;

/**
 * Created by emiranda on 23-02-2015.
 */
public class Spacey extends ObservableActor {

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Spacey") == 0);
    }
}
