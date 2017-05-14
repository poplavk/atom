package ru.atom;

import ru.atom.game.server.model.Bomb;
import ru.atom.game.server.model.Brick;
import ru.atom.game.server.model.GameSession;
import ru.atom.game.server.model.Girl;
import ru.atom.game.server.model.Bonus;

/**
 * Create sample game session with all kinds of objects that will present in bomber-man game
 */
public final class TestGameSessionCreator {
    private TestGameSessionCreator() {
    }

    static GameSession createGameSession() {
        GameSession gameSession = new GameSession();
        gameSession.addGameObject(new Brick(0, 0, Brick.BrickType.UNBREACABLE));
        gameSession.addGameObject(new Brick(0, 4, Brick.BrickType.UNBREACABLE));

        gameSession.addGameObject(new Girl(0, 2));
        gameSession.addGameObject(new Girl(1, 2));

        gameSession.addGameObject(new Bomb(2, 2, 15));
        gameSession.addGameObject(new Bomb(3, 3, 15));

        gameSession.addGameObject(new Bonus(4, 4, Bonus.BonusType.FIRE));
        gameSession.addGameObject(new Bonus(5, 5, Bonus.BonusType.SPEED));

        return gameSession;
    }
}
