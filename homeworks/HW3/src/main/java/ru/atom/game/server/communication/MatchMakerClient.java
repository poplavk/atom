package ru.atom.game.server.communication;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MatchMakerClient {
    private static final Logger log = LogManager.getLogger(MatchMakerClient.class);
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
                .url(PROTOCOL + HOST + PORT + "/mm/add-match")
                .build();
        return client.newCall(request).execute();
    }

    public static Response addResult(Integer id,String name, Integer result ) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("match", String.valueOf(id))
                .add("name", name)
                .add("result", String.valueOf(result))
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .header("Authorization", "Bearer " + MatchMakerClient.serverToken)
                .url(PROTOCOL + HOST + PORT + "/mm/add-result")
                .build();
        return client.newCall(request).execute();
    }


    public static void setServerToken(String serverToken) {
        MatchMakerClient.serverToken = serverToken;
    }
}


