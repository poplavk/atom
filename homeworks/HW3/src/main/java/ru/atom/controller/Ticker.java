package ru.atom.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.dbhackaton.server.base.Match;
import ru.atom.dbhackaton.server.mm.Connection;
import ru.atom.dbhackaton.server.service.MatchMakerService;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.Bomb;
import ru.atom.model.GameObject;
import ru.atom.model.GameSession;
import ru.atom.network.Broker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Ticker implements Runnable {
    private static final Logger log = LogManager.getLogger(Ticker.class);
    private static final int FPS = 2;
    private static final long FRAME_TIME = 1000 / FPS;
    private long tickNumber = 0;

    public static final int PLAYERS_IN_GAME = 1;
    private final Integer id;

    private final Broker broker = Broker.getInstance();
    private final Set<String> players = new CopyOnWriteArraySet<>();
    private final HashMap<Integer, String> girlsIdToPlayer = new HashMap<Integer, String>();

    private final GameSession gameSession = new GameSession();


    public Ticker() {
        this.id = MatchMakerService.saveMatch(new Match());
    }

    public long getId() {
        return id;
    }

    public boolean canAddPlayer(String player) {
        return players.size() < PLAYERS_IN_GAME && players.add(player);
    }

    public boolean canRestorePlayer(@NotNull String player) {
        if (players.contains(player)) {
            broker.send(player,
                    Topic.POSSESS,
                    girlsIdToPlayer.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(player))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElseGet(null));
            return true;
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

    private void act(long time) {
        //Your logic here
        gameSession.tick(time);
        players.forEach(player -> {
            broker.send(player, Topic.REPLICA, gameSession.getGameObjects());
        });
    }

    public long getTickNumber() {
        return tickNumber;
    }

    private boolean tick() {
        long started = System.currentTimeMillis();
        act(FRAME_TIME);
        long elapsed = System.currentTimeMillis() - started;
        if (elapsed < FRAME_TIME) {
            log.info("All tick finish at {} ms", elapsed);
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
        } else {
            log.warn("tick lag {} ms", elapsed - FRAME_TIME);
        }
        log.info("{}: tick ", tickNumber);
        tickNumber++;
        return true;
    }

    @Override
    public void run() {
        girlsIdToPlayer.forEach( (id, player) -> {
            broker.send(player, Topic.POSSESS, id);
        });

        while (!Thread.currentThread().isInterrupted()) {
            tick(); //todo handle game over
        }
    }
}
