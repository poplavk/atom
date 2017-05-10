package ru.atom.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ru.atom.util.JsonHelper;

public class Message {
    private final Topic topic;
    private final String data;

    // TODO: 4/30/17 продумать вложенную стуктуру data с правильным мапингом в json (пока костыль)

    public Message(Topic topic, Object data) {
        this.topic = topic;
        if (topic == Topic.HELLO) {
            this.data = data.toString();
            return;
        }
        if (topic == Topic.MOVE) {
            this.data = JsonHelper.toJson(data);
            return;
        }
        if (topic == Topic.PLANT_BOMB) {
            this.data = "";
            return;
        }
        this.data = data.toString();
    }

    @JsonCreator
    public Message(@JsonProperty("topic") Topic topic, @JsonProperty("data") JsonNode data) {
        this.topic = topic;
        this.data = data.asText();
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
        return data;
    }
}
