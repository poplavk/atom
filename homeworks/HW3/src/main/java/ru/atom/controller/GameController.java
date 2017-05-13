package ru.atom.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.dbhackaton.server.mm.*;
import ru.atom.geometry.Point;
import ru.atom.message.DirectionMsg;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.*;
import ru.atom.model.GameSession;
import ru.atom.util.JsonHelper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameController {
    private static final Logger log = LogManager.getLogger(GameController.class);

    private final ConcurrentHashMap<String, Girl> playerToGirl = new ConcurrentHashMap<>();
    private final List<Ticker> tickers = new CopyOnWriteArrayList<>();

    private static final Object lock = new Object();

    //TODO maybe make it singleton?
    static public GameController getInstance() {
        return new GameController();
    }

    public GameController() {

    }

    private boolean addPlayerToTicker(String player, Ticker ticker) {
        if (ticker.canAddPlayer(player)) {
            Girl girl = new Girl(new Point(GameSession.TILE_SIZE,GameSession.TILE_SIZE)); //TODO remake it
            if (playerToGirl.putIfAbsent(player, girl) == null) {
                ticker.addPlayer(girl, player);
                return true;
            }
        }
        return false;
    }

    public void addPlayerAndStartGame(String player) {
        //TODO choose game session
        synchronized (lock) {
            for (Ticker ticker : tickers) {
                if (ticker.canRestorePlayer(player)) {
                    return;
                }

                if (addPlayerToTicker(player, ticker)) {
                    if (ticker.canStartGame()) {

                        Thread thread = new Thread(ticker);
                        thread.start();
                        log.info("Game started");
                    }
                    return;
                }
            }
            Ticker ticker = new Ticker();
            if (addPlayerToTicker(player, ticker)) {
                tickers.add(ticker);
                if (ticker.canStartGame()) {

                    Thread thread = new Thread(ticker);
                    thread.start();
                    log.info("Game started");
                }
            }
        }
    }

    public void onMsgHandler(@NotNull String player, @NotNull Message msg) {
        Girl girl = playerToGirl.get(player);
        if (girl == null) {
            log.warn("try to do something with null girl");
            return;
        }
        Topic topic = msg.getTopic();
        String json = msg.getData();
        log.info("msg.data: {}", json);
        if (topic == Topic.MOVE) {
            DirectionMsg directionMsg = JsonHelper.fromJson(json, DirectionMsg.class);
            // TODO: 5/13/17 move в тике?????
            girl.move(directionMsg.getDirection());
            return;
        }
        if (topic==Topic.PLANT_BOMB) {
            girl.plantBomb();
            return;
        }
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

}
