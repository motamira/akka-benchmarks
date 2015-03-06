package pt.aigx.actors;

public class Pitt extends MasterActor {

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Pitt") == 0);
    }

    @Override
    public long getProcessingDelay() {
        return 0;
    }
}