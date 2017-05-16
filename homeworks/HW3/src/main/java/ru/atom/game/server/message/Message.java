package ru.atom.game.server.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.game.server.util.JsonHelper;

public class Message {
    private final Topic topic;
    private final Object data;

    public Message(Topic topic, Object data) {
        this.topic = topic;
        switch (topic) {
            case MOVE:
                this.data = JsonHelper.getJsonNode(JsonHelper.toJson(data));
                break;
            case HELLO:
            case POSSESS:
                this.data = data.toString();
                break;
            case PLANT_BOMB:
                this.data = "";
                break;
            case REPLICA:
                this.data = data;
                break;
            default:
                this.data = data.toString();
        }
    }

    @JsonCreator
    public Message(@JsonProperty("topic") Topic topic, @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        if (data.isTextual()) {
            this.data = data.asText();
        } else {
            this.data = data;
        }

    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (topic != message.topic) return false;
        return data != null ? data.equals(message.data) : message.data == null;
    }

    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public String getData() {
        return data.toString();
    }
}
