package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAuthDataAccess implements AuthDataAccess {

    public MySQLAuthDataAccess() throws Exception {
        configureDatabase();
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException, SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertAuth(connection, authData.authToken(), authData.username());
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
    public void clear() {

    }

    private void insertAuth(Connection conn, String authToken, String username) throws SQLException {
        try (var preparedStatement = conn.prepareStatement(
                "INSERT INTO authData (authToken, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData(
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`)
            )
            """
    };

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(); // EDIT THIS LATER
        }
    }
}
