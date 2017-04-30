package ru.atom.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Message {
    private final Topic topic;
    private final String data;

    // TODO: 4/30/17 продумать вложенную стуктуру data с правильным мапингом в json (пока костыль)

    public Message(Topic topic, String data) {
        this.topic = topic;
        if (topic == Topic.HELLO) {
            this.data = data;
            return;
        }
        if (topic == Topic.MOVE) {
            this.data = "{  \"direction\" : " + "\"" + data + "\" }";
            return;
        }
        if (topic == Topic.PLANT_BOMB) {
            this.data = "";
            return;
        }
        this.data = data;
    }

    @JsonCreator
    public Message(@JsonProperty("topic") Topic topic, @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        this.data = data.toString();
    }

    public Topic getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }
}
