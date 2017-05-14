package ru.atom.game.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.geometry.Point;
import ru.atom.game.server.message.DirectionMsg;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Girl;
import ru.atom.game.server.model.Movable;
import ru.atom.game.server.util.JsonHelper;

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
    private final ConcurrentHashMap<Girl, Ticker> girlToTicker = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    //TODO maybe make it singleton?
    public static GameController getInstance() {
        return new GameController();
    }

    public GameController() {

    }

    private boolean addPlayerToTicker(String player, Ticker ticker) {
        if (ticker.canAddPlayer(player)) {
            Girl girl = new Girl(new Point(GameSession.TILE_SIZE,GameSession.TILE_SIZE)); //TODO remake it
            if (playerToGirl.putIfAbsent(player, girl) == null) {
                ticker.addPlayer(girl, player);
                girlToTicker.put(girl, ticker);
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
        Ticker ticker = girlToTicker.get(girl);
        Topic topic = msg.getTopic();
        String json = msg.getData();
        log.info("msg.data: {}", json);
        if (topic == Topic.MOVE) {
            DirectionMsg directionMsg = JsonHelper.fromJson(json, DirectionMsg.class);
            // TODO: 5/13/17 move в тике?????
            ticker.move(girl, directionMsg.getDirection());
//            girl.move(directionMsg.getDirection());
            return;
        }
        if (topic == Topic.PLANT_BOMB) {
            girl.plantBomb();
            return;
        }
    }


    public List<Ticker> getTickers() {
        return tickers;
    }

}
