package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void addBad() {
        GameData badGameData = new GameData(1, "Granny", "Grandpa",
                "Where's the game??", null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDataAccess.addGame(badGameData));
    }

    @Test
    void getGameGood() {
    }

    @Test
    void getGameBad() {
    }

    @Test
    void clear() {
    }

    @Test
    void getSizeGood() {
    }

    @Test
    void getSizeBad() {
    }

    @Test
    void getIDsGood() {
    }

    @Test
    void getIDsBad() {
    }

    @Test
    void listGamesGood() {
    }

    @Test
    void listGamesBad() {
    }
}