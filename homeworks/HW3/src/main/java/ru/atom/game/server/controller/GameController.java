package ru.atom.game.server.controller;

import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.server.communication.MatchMakerClient;
import ru.atom.game.server.geometry.Point;
import ru.atom.game.server.message.DirectionMsg;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Girl;

import ru.atom.game.server.network.Broker;
import ru.atom.game.server.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameController {
    private static final Logger log = LogManager.getLogger(GameController.class);

    private final ConcurrentHashMap<String, Girl> playerToGirl = new ConcurrentHashMap<>();
    private final List<Ticker> tickers = new ArrayList<>();

    private static final Object lock = new Object();

    private static final GameController instance = new GameController();


    public static GameController getInstance() {
        return instance;
    }

    public GameController() {

    }

    private boolean addPlayerToTicker(String player, Ticker ticker) {
        int playersLeft = ticker.canAddPlayer(player);
        if (playersLeft != 0) {
            int posX;
            int posY;
            switch (playersLeft) {
                case 4:
                    posX = GameSession.TILE_SIZE;
                    posY = GameSession.TILE_SIZE;
                    break;
                case 3:
                    posX = GameSession.TILE_SIZE * (GameSession.TILES_X - 2);
                    posY = GameSession.TILE_SIZE;
                    break;
                case 2:
                    posX = GameSession.TILE_SIZE;
                    posY = GameSession.TILE_SIZE * (GameSession.TILES_Y - 2);
                    break;
                case 1:
                    posX = GameSession.TILE_SIZE * (GameSession.TILES_X - 2);
                    posY = GameSession.TILE_SIZE * (GameSession.TILES_Y - 2);
                    break;
                default:
                    posX = 0;
                    posY = 0;
            }

            Girl girl = new Girl(new Point(posX, posY));
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
            girl.setDirection(directionMsg.getDirection());
//            move(ticker, girl, directionMsg.getDirection());
//            ticker.move(girl, directionMsg.getDirection());
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

    public void removeTicker(Ticker ticker) {
        synchronized (lock) {
            tickers.remove(ticker);
        }
        log.info("Ticker was removed");
    }

    public void removePlayer(String player, Integer gameId, boolean isWinner) {
        playerToGirl.remove(player);
        log.info("remove player: {}", player);
        String status = isWinner ? "win" : "lose";
        Broker.getInstance().send(player, Topic.END_MATCH, status);
        int result = isWinner ? 1 : 0;
        try {
            Response response = MatchMakerClient.addResult(gameId, player, result);
            log.info("response to save math: {} {} {}",
                    response.toString(),
                    response.body().toString(),
                    response.message().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
