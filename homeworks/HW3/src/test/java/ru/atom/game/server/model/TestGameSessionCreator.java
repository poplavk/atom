package ru.atom.game.server.model;

import ru.atom.game.server.geometry.Point;

/**
 * Create sample game session with all kinds of objects that will present in bomber-man game
 */
public final class TestGameSessionCreator {
    private TestGameSessionCreator() {
    }

    static GameSession createGameSession() {
        GameSession gameSession = new GameSession();
        gameSession.addGameObject(new Tile(new Point(0, 0), Tile.TileType.Wall));
        gameSession.addGameObject(new Tile(new Point(0, 4), Tile.TileType.Wall));

        gameSession.addGameObject(new Girl(new Point(0, 2)));
        gameSession.addGameObject(new Girl(new Point(1, 2)));

        gameSession.addGameObject(new Bonus(new Point(4, 4), Bonus.BonusType.FIRE));
        gameSession.addGameObject(new Bonus(new Point(5, 5), Bonus.BonusType.SPEED));

        gameSession.addGameObject(new Fire(new Point(8,8)));

        return gameSession;
    }
}
