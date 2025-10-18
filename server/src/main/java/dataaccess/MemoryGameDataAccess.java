package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {
    private HashMap<String, AuthData> game = new HashMap<>();

    @Override
    public void addGame(GameData gameData) {

    }

    @Override
    public GameData getGame(GameData gameData) {
        return null;
    }

    @Override
    public void deleteGame(GameData gameData) {

    }

    @Override
    public void clear() {
        game.clear();
    }
}
