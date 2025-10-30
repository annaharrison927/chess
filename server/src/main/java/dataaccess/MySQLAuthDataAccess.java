package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAuthDataAccess implements AuthDataAccess {

    public MySQLAuthDataAccess() throws DataAccessException {
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertAuth(connection, authData.authToken(), authData.username());
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex); // EDIT THIS LATER
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            clearAuth(connection);
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private void insertAuth(Connection connection, String authToken, String username) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(
                "INSERT INTO authData (authToken, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
        }
    }

    private void clearAuth(Connection connection) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement(
                "TRUNCATE TABLE authData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

}
