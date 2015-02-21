package pt.aigx.actors.messages;

import java.io.Serializable;

public class SimpleStringMessage implements Serializable {

    private final String text;

    public SimpleStringMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}