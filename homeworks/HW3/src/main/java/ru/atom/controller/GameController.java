package ru.atom.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.atom.geometry.Point;
import ru.atom.message.Message;
import ru.atom.message.Topic;
import ru.atom.model.GameObject;
import ru.atom.model.GameSession;
import ru.atom.model.Girl;
import ru.atom.network.Broker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dmbragin on 5/3/17.
 */
public class GameController {
    private static final Logger log = LogManager.getLogger(GameController.class);

    private final ConcurrentHashMap<String, Girl> playerToGirl;
    private GameSession gameSession = new GameSession();

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
            }
        }

    }

    public void onMsgHandler(String player, @NotNull Message msg) {
        Topic topic = msg.getTopic();
        if (topic == Topic.MOVE) {
            // TODO: 4/30/17 вытащить направление, по тику передвинуть девочку
            return;
        }
        if (topic==Topic.PLANT_BOMB) {
            // TODO: 4/30/17 на текущей координате девочки разместить бомбу
            return;
        }
    }

}
