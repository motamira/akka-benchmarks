package pt.aigx.actors;

/**
 * Created by emiranda on 23-02-2015.
 */
public class Shakespeare extends ObservableActor {

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Shakespeare") == 0);
    }
}
