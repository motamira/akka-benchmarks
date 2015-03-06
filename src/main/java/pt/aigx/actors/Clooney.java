package pt.aigx.actors;

public class Clooney extends MasterActor {

    @Override
    public boolean isMessageForMe(String text) {
        return (text.compareTo("Clooney") == 0);
    }

    @Override
    public long getProcessingDelay() {
        return 0;
    }
}
