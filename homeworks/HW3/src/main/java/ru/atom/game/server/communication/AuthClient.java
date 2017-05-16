package ru.atom.game.server.communication;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class AuthClient {
    private static final OkHttpClient client = new OkHttpClient();
    //TODO add properties
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    public static Response isLogined(String token) throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Authorization", "Bearer " + token)
                .url(PROTOCOL + HOST + PORT + "/auth/is-logined")
                .build();
        return client.newCall(request).execute();
    }
}

