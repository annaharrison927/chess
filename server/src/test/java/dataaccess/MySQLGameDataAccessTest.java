package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

class MySQLGameDataAccessTest {

    private static MySQLGameDataAccess gameDataAccess;

    @BeforeAll
    public static void init() {
        gameDataAccess = new MySQLGameDataAccess();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDataAccess.clear();
    }

    @Test
    void addGameGood() {
        ChessGame goodGame = new ChessGame();
        GameData goodGameData = new GameData(1, "Elton John", "Billy Joel",
                "Long long game", goodGame);
        Assertions.assertDoesNotThrow(() -> {
            gameDataAccess.addGame(goodGameData);
        });
    }

    @Test
    void addBad() throws DataAccessException {
        GameData badGameData = new GameData(-1, "Granny", "Grandpa",
                "Where's the game??", null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDataAccess.addGame(badGameData));
    }

    @Test
    void getGameGood() throws DataAccessException {
        ChessGame goodGame = new ChessGame();
        GameData goodGameData = new GameData(1, "Elton John", "Billy Joel",
                "Long long game", goodGame);
        gameDataAccess.addGame(goodGameData);

        Assertions.assertDoesNotThrow(() -> {
            gameDataAccess.getGame(1);
        });
    }

    @Test
    void getGameBad() throws DataAccessException {
        Assertions.assertNull(gameDataAccess.getGame(1));
    }

    @Test
    void clear() throws DataAccessException {
        ChessGame goodGame = new ChessGame();
        GameData goodGameData = new GameData(1, "Batman", "Robin",
                "Superman Stinks", goodGame);
        gameDataAccess.addGame(goodGameData);

        Assertions.assertDoesNotThrow(() -> {
            gameDataAccess.clear();
        });
    }

    @Test
    void getSizeGood() throws DataAccessException {
        ChessGame superGame = new ChessGame();
        GameData superGameData = new GameData(1, "Batman", "Robin",
                "Superman Stinks", superGame);
        gameDataAccess.addGame(superGameData);

        ChessGame goodGame = new ChessGame();
        GameData goodGameData = new GameData(2, "Elton John", "Billy Joel",
                "Long long game", goodGame);
        gameDataAccess.addGame(goodGameData);

        Assertions.assertEquals(2, gameDataAccess.getSize());
    }

    @Test
    void getSizeBad() throws DataAccessException {
        Assertions.assertEquals(0, gameDataAccess.getSize());
    }

    @Test
    void getIDsGood() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(3, "Bingo", "Bluey", "Playtime!",
                game);
        gameDataAccess.addGame(gameData);

        Assertions.assertDoesNotThrow(() -> {
            gameDataAccess.getIDs();
        });
    }

    @Test
    void getIDsBad() throws DataAccessException {
        Set<Integer> emptyIDSet = gameDataAccess.getIDs();
        Assertions.assertTrue(emptyIDSet.isEmpty());
    }

    @Test
    void listGamesGood() throws DataAccessException {
        ChessGame goodGame = new ChessGame();
        GameData goodGameData = new GameData(2, "Elton John", "Billy Joel",
                "Long long game", goodGame);
        gameDataAccess.addGame(goodGameData);

        ChessGame game = new ChessGame();
        GameData gameData = new GameData(3, "Bingo", "Bluey", "Playtime!",
                game);
        gameDataAccess.addGame(gameData);

        Assertions.assertDoesNotThrow(() -> {
            gameDataAccess.listGames();
        });
    }

    @Test
    void listGamesBad() throws DataAccessException {
        HashMap<Integer, GameData> emptyGameList = gameDataAccess.listGames();
        Assertions.assertTrue(emptyGameList.isEmpty());
    }
}