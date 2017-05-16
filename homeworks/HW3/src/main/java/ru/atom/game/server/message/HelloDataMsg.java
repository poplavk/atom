package ru.atom.game.server.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HelloDataMsg {
    private String user;
    private String token;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public HelloDataMsg(@JsonProperty("name") String user, @JsonProperty("token") String token) {
        this.user = user;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
