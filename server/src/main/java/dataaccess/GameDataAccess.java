package dataaccess;

import model.GameData;

public interface GameDataAccess {
    void addGame(GameData gameData);

    GameData getGame(GameData gameData);

    void deleteGame(GameData gameData);

    void clear();
}
