package ru.atom.game.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Response;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.communication.MatchMakerClient;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.GameObject;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Girl;
import ru.atom.game.server.model.Movable;
import ru.atom.game.server.network.Broker;
import sun.awt.windows.ThemeReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

public class Ticker implements Runnable {
    private static final long MAX_TICK = 100000;
    private static final Logger log = LogManager.getLogger(Ticker.class);
    private static final int FPS = 60;
    private static final long FRAME_TIME = 1000 / FPS;
    private long tickNumber = 0;

    public static final int PLAYERS_IN_GAME = 1;
    private final Integer id;

    private final Broker broker = Broker.getInstance();
    private final GameController gameController = GameController.getInstance();

    private final Set<String> players = new HashSet<>();
    private final HashMap<Integer, String> girlsIdToPlayer = new HashMap<Integer, String>();

    private final GameSession gameSession = new GameSession(this);


    public Ticker() {
        Integer id1;
        okhttp3.Response response = null;
        try {
            response = MatchMakerClient.addMatch();
            if (response.code() == 200) {
                id1 = Integer.valueOf(response.body().string());
            } else {
                id1 = 0;
            }
            log.info("response to add math: {}", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            id1 = 0;
        }

        this.id = id1;
    }

    public long getId() {
        return id;
    }

    public boolean canAddPlayer(String player) {
        return players.size() < PLAYERS_IN_GAME && players.add(player);
    }

    public boolean canRestorePlayer(@NotNull String player) {
        if (players.contains(player)) {
            List<Integer> ids = girlsIdToPlayer.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(player))
                    .map(Map.Entry::getKey).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                broker.send(player, Topic.POSSESS, ids.get(0));
                return true;
            }
        }
        return false;
    }


    public boolean canStartGame() {
        return players.size() == PLAYERS_IN_GAME;
    }

    public void addPlayer(GameObject gameObject, String player) {
        girlsIdToPlayer.put(gameObject.getId(), player);
        gameSession.addGameObject(gameObject);
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    private boolean act(long time) {
        //Your logic here
        //TODO gameSession must return deadPlayers
        List<Integer> deadPlayers = gameSession.tick(time);

        deadPlayers.forEach(id -> {
            String player = girlsIdToPlayer.get(id);
            girlsIdToPlayer.remove(id);
            gameController.removePlayer(player, id, false);
        });

        girlsIdToPlayer.values().forEach(player -> {
            broker.send(player, Topic.REPLICA, gameSession.getGameObjects());
        });

        return girlsIdToPlayer.size() != 0;
    }

    public long getTickNumber() {
        return tickNumber;
    }

    private boolean tick() {
        long started = System.currentTimeMillis();
        boolean gameContinue = act(FRAME_TIME);
        long elapsed = System.currentTimeMillis() - started;
        if (elapsed < FRAME_TIME) {
            log.info("All tick finish at {} ms", elapsed);
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
        } else {
            log.warn("tick lag {} ms", elapsed - FRAME_TIME);
        }
        log.info("{}: tick ", tickNumber);
        tickNumber++;

        //TODO add normal game over

        return gameContinue && tickNumber <= MAX_TICK;
    }

    @Override
    public void run() {
        girlsIdToPlayer.forEach((id, player) -> {
            broker.send(player, Topic.POSSESS, id);
        });

        while (tick() && !Thread.currentThread().isInterrupted()) {

        }
        gameController.removeTicker(this);
        girlsIdToPlayer.values().forEach(s -> {
            gameController.removePlayer(s, id, true);
        });
    }


    public synchronized void removePlayers(ArrayList<Girl> girls) {
        for (Girl girl: girls) {
            String player = girlsIdToPlayer.get(girl.getId());
            players.remove(player);
        }
    }
}
