package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class MemoryGameDataAccess implements GameDataAccess {
    private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void addGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public int getSize() {
        return games.size();
    }

    @Override
    public Set<Integer> getIDs() {
        return games.keySet();
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        return games;
    }


}
