package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Set;

public interface GameDataAccess {
    void addGame(GameData gameData);

    GameData getGame(int gameID);

    void clear();

    int getSize();

    Set<Integer> getIDs();

    HashMap<Integer, GameData> listGames();
}
