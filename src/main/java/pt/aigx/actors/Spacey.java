package pt.aigx.actors;

public class Spacey extends MasterActor {

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Spacey") == 0);
    }

    @Override
    public long getProcessingDelay() {
        return 0;
    }
}
