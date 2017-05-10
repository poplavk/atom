package ru.atom.model;

import ru.atom.geometry.Point;

/**
 * Create sample game session with all kinds of objects that will present in bomber-man game
 */
public final class TestGameSessionCreator {
    private TestGameSessionCreator() {
    }

    static GameSession createGameSession() {
        GameSession gameSession = new GameSession();
        gameSession.addGameObject(new Wall(new Point(0, 0), Wall.BrickType.UNBREACABLE));
        gameSession.addGameObject(new Wall(new Point(0, 4), Wall.BrickType.UNBREACABLE));

        gameSession.addGameObject(new Girl(new Point(0, 2)));
        gameSession.addGameObject(new Girl(new Point(1, 2)));

        gameSession.addGameObject(new Bonus(new Point(4, 4), Bonus.BonusType.FIRE));
        gameSession.addGameObject(new Bonus(new Point(5, 5), Bonus.BonusType.SPEED));

        return gameSession;
    }
}
