package ru.atom.game.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.GameObject;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.network.Broker;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

public class Ticker implements Runnable {
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

    private final GameSession gameSession = new GameSession();


    public Ticker() {
        //TODO add save info to db
        //        this.id = MatchMakerService.saveMatch(new Match());
        this.id = 0;

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

    private void act(long time) {
        //Your logic here
        List<Integer> deadPlayers = new ArrayList<>();
        //TODO gameSession must return deadPlayers
        gameSession.tick(time);

        //TODO remove it
        if (tickNumber == 500) {
            deadPlayers.addAll(girlsIdToPlayer.keySet());
        }


        deadPlayers.forEach(id -> {
            String player = girlsIdToPlayer.get(id);
            girlsIdToPlayer.remove(id);
            gameController.removePlayer(player);
        });

        girlsIdToPlayer.values().forEach(player -> {
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

        //TODO add normal game over
        return tickNumber <= 1000;
    }

    @Override
    public void run() {
        girlsIdToPlayer.forEach((id, player) -> {
            broker.send(player, Topic.POSSESS, id);
        });

        while (!Thread.currentThread().isInterrupted()) {
            if (!tick()) {
                Thread.currentThread().interrupt();
                gameController.removeTicker(this);
                girlsIdToPlayer.values().forEach(gameController::removePlayer);
            }
        }
    }
}
