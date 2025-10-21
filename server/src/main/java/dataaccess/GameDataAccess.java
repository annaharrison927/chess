package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameDataAccess {
    void addGame(GameData gameData);

    GameData getGame(int gameID);

    void deleteGame(GameData gameData);

    void clear();

    int getSize();

    Set<Integer> getIDs();
}
