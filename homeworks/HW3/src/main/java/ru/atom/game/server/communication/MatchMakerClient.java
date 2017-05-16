package ru.atom.game.server.communication;

import okhttp3.*;

import java.io.IOException;

public class MatchMakerClient {
    private static final OkHttpClient client = new OkHttpClient();
    //TODO add properties
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    private static String serverToken = null;

    public static Response loginServer(String login, String passwd) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("user", login)
                .add("password", passwd)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(PROTOCOL + HOST + PORT + "/auth/login")
                .build();
        return client.newCall(request).execute();
    }

    public static Response isUserLogined(String token) throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Authorization", "Bearer " + token)
                .url(PROTOCOL + HOST + PORT + "/auth/is-logined")
                .build();
        return client.newCall(request).execute();
    }

    public static Response addMatch() throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Authorization", "Bearer " + MatchMakerClient.serverToken)
                .url(PROTOCOL + HOST + PORT + "/auth/add-match")
                .build();
        return client.newCall(request).execute();
    }

    public static Response addResult(Integer id,String name, Integer result ) throws IOException {
        Request request = new Request.Builder()
                .get()
                .header("Authorization", "Bearer " + MatchMakerClient.serverToken)
                .url(PROTOCOL + HOST + PORT + "/auth/add-result")
                .build();
        return client.newCall(request).execute();
    }


    public static void setServerToken(String serverToken) {
        MatchMakerClient.serverToken = serverToken;
    }
}


