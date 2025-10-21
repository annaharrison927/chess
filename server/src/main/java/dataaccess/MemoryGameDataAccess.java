package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Set;

public class MemoryGameDataAccess implements GameDataAccess {
    private HashMap<Integer, GameData> game = new HashMap<>();

    @Override
    public void addGame(GameData gameData) {
        game.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void deleteGame(GameData gameData) {

    }

    @Override
    public void clear() {
        game.clear();
    }

    @Override
    public int getSize() {
        return game.size();
    }

    @Override
    public Set<Integer> getIDs() {
        return game.keySet();
    }


}
