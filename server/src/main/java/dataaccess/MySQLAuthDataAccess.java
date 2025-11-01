package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLAuthDataAccess implements AuthDataAccess {

    public MySQLAuthDataAccess() throws DataAccessException {
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertAuth(connection, authData.authToken(), authData.username());
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            AuthData authData = retrieveAuth(connection, authToken);
            if (authData.authToken() == null) {
                return null;
            } else {
                return authData;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            clearOneAuth(connection, authToken);
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            clearAllAuth(connection);
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

    private AuthData retrieveAuth(Connection conn, String authToken) throws DataAccessException {
        AuthData authData = new AuthData(null, null);
        try (var preparedStatement = conn.prepareStatement(
                "SELECT authToken, username FROM authData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");

                    authData = new AuthData(authToken, username);
                }
            }
            return authData;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private void clearAllAuth(Connection connection) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement(
                "TRUNCATE TABLE authData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private void clearOneAuth(Connection connection, String authToken) throws DataAccessException {
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM authData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

}
