package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MySQLGameDataAccess implements GameDataAccess {
    @Override
    public void addGame(GameData gameData) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertGame(connection, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return retrieveSize(connection);
        } catch (
                SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public Set<Integer> getIDs() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return retrieveIDs(connection);
        } catch (
                SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return retrieveGames(connection);
        } catch (
                SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private int retrieveSize(Connection conn) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement(
                "SELECT COUNT(*) AS game_count FROM gameData")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                int game_count = 0;
                if (rs.next()) {
                    game_count = rs.getInt("game_count");
                }
                return game_count;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private Set<Integer> retrieveIDs(Connection conn) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement(
                "SELECT gameID FROM gameData")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                Set<Integer> gameIDs = new HashSet<>() {
                };
                while (rs.next()) {
                    int gameID = rs.getInt("gameID");
                    gameIDs.add(gameID);
                }
                return gameIDs;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private HashMap<Integer, GameData> retrieveGames(Connection conn) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement(
                "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                HashMap<Integer, GameData> games = new HashMap<>();
                while (rs.next()) {
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String gameJson = rs.getString("game");

                    // Deserialize the game object
                    ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);

                    GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);

                    games.put(gameID, gameData);
                }
                return games;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private void insertGame(Connection conn, int gameID, String whiteUsername, String blackUsername, String gameName,
                            ChessGame game) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement(
                "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) " +
                        "VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUsername);
            preparedStatement.setString(3, blackUsername);
            preparedStatement.setString(4, gameName);

            // Serialize and store the chess game object
            var game_json = new Gson().toJson(game);
            preparedStatement.setString(5, game_json);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }
}
