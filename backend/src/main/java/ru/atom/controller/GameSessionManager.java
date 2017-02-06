package ru.atom.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.network.ConnectionPool;
import ru.atom.network.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by sergey on 2/6/17.
 */
public class GameSessionManager implements Runnable {
    private static final Logger log = LogManager.getLogger(GameSessionManager.class);
    private static final BlockingQueue<Player> queue = new LinkedBlockingQueue<>();
    public static final int PLAYERS_IN_GAME = 1;
    private final Executor executor;
    private final ConcurrentHashMap<GameSession, GameSession> gameSessions;

    public GameSessionManager() {
        this.gameSessions = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(1);
    }

    public int getGameSessionsNumber() {
        return gameSessions.size();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<Player> players = new ArrayList<>();

                while (players.size() < PLAYERS_IN_GAME) {
                    players.add(queue.take());
                }
                newSession(players);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void newSession(@NotNull List<Player> players) {
        log.info("Full house. Starting new game with {}", players.stream().map(Player::getName).collect(Collectors.toList()));
        GameSession session = new GameSession(players);
        gameSessions.put(session, session);

        CompletableFuture.runAsync(session, executor)
                .handle((ignored, ex) -> {
                    gameSessions.remove(session);
                    players.forEach(x -> ConnectionPool.remove(x.getSession()));
                    if (ex == null) {
                        log.info("Session {} finished", session);
                    } else {
                        log.error("Session {} terminated", session, ex);
                    }
                    return null;
                });
    }

    public static void register(Player player) {
        ConnectionPool.add(player.getSession(), player);
        try {
            queue.put(player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}