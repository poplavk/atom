package ru.atom.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.geometry.Point;
import ru.atom.message.DirectionMsg;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.*;
import ru.atom.network.Broker;
import ru.atom.util.JsonHelper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameController {
    private static final Logger log = LogManager.getLogger(GameController.class);

    private final ConcurrentHashMap<String, Girl> playerToGirl;
    private GameSession gameSession = new GameSession();
    private Ticker ticker = null;

    //TODO maybe make it singleton?
    static public GameController getInstance() {
        return new GameController();
    }

    public GameController() {
        this.playerToGirl = new ConcurrentHashMap<>();
        generateMap();
    }

    private void generateMap() {
        // TODO add map generator
        gameSession.addGameObject(new Bomb(new Point(2, 2), 15));
        gameSession.addGameObject(new Bomb(new Point(3, 3), 15));

        gameSession.addGameObject(new Bonus(new Point(4, 4), Bonus.BonusType.FIRE));
        gameSession.addGameObject(new Bonus(new Point(5, 5), Bonus.BonusType.SPEED));
    }

    public void addPlayerAndStartGame(String player) {
        //TODO choose game session
        int size = playerToGirl.size();
        if (size < 4) {
            Girl girl = new Girl(new Point(0,0)); //TODO remake it
            gameSession.addGameObject(girl);
            if (playerToGirl.putIfAbsent(player, girl) == null && playerToGirl.size() == 4) {
                Broker.getInstance().broadcast(Topic.POSSESS, "");
                log.info("Game started");
                Broker.getInstance().broadcast(Topic.REPLICA, gameSession.getGameObjects());
                ticker = new Ticker(gameSession);
                ticker.loop();
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
        log.info("msg: {}", json);
        if (topic == Topic.MOVE) {
            DirectionMsg directionMsg = JsonHelper.fromJson(json, DirectionMsg.class);
            girl.move(directionMsg.getDirection());
            return;
        }
        if (topic==Topic.PLANT_BOMB) {
            girl.plantBomb();
            return;
        }
    }

}
