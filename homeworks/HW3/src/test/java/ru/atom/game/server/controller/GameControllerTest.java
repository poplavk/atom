package ru.atom.game.server.controller;

import org.junit.Before;
import org.junit.Test;
import ru.atom.game.server.geometry.Point;
import ru.atom.game.server.message.DirectionMsg;
import ru.atom.game.server.message.Message;
import ru.atom.game.server.message.Topic;
import ru.atom.game.server.model.GameObject;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Girl;
import ru.atom.game.server.model.Movable;

import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by dmbragin on 5/3/17.
 */
public class GameControllerTest {
    private final GameController gameController = GameController.getInstance();

    @Before
    public void init() {
        for (Integer i = 0; i < 4; i++) {
            gameController.addPlayerAndStartGame(i.toString());
        }
    }

    @Test
    public void testStartGame() {
        int gamesCount = gameController.getTickers().size();
        for (Integer i = 4; i < 9; i++) {
            gameController.addPlayerAndStartGame(i.toString());
        }
        assertEquals(gamesCount + 2, gameController.getTickers().size());
    }

    @Test
    public void testMoveActions() {
        GameSession gameSession = gameController.getTickers().get(0).getGameSession();
        List<GameObject> objects = gameSession.getGameObjects();
        List<Girl> girls = objects.stream()
                .filter(o -> o instanceof Girl)
                .map(o -> (Girl) o).collect(Collectors.toList());
        Point oldPoint = girls.get(0).getPosition();
        for (Integer i = 0; i < 4; i++) {
            Message msg = new Message(Topic.MOVE, new DirectionMsg(Movable.Direction.UP));
            gameController.onMsgHandler(i.toString(), msg);
        }

        gameSession.tick(10);
        assertTrue(oldPoint.getY() < girls.get(0).getPosition().getY());
    }

    @Test
    public void testPlantBombActions() {
        GameSession gameSession = gameController.getTickers().get(0).getGameSession();
        List<GameObject> objects = gameSession.getGameObjects();
        List<Girl> girls = objects.stream()
                .filter(o -> o instanceof Girl)
                .map(o -> (Girl) o).collect(Collectors.toList());
        int oldSize = objects.size();
        for (Integer i = 0; i < 4; i++) {
            Message msg = new Message(Topic.PLANT_BOMB, "");
            gameController.onMsgHandler(i.toString(), msg);
        }

        gameSession.tick(10);

        assertTrue(oldSize + 4 == gameSession.getGameObjects().size());
    }



}