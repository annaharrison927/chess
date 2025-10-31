package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Set;

public interface GameDataAccess {
    void addGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID);

    void clear();

    int getSize() throws DataAccessException;

    Set<Integer> getIDs() throws DataAccessException;

    HashMap<Integer, GameData> listGames();
}
