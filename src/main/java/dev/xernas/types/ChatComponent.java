package dev.xernas.types;

import org.json.JSONObject;

public class ChatComponent {

    private String text;

    public ChatComponent() {}

    public ChatComponent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("text", text);
        return json.toString();
    }

}
