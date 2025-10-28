package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUserDataAccess implements UserDataAccess {
    @Override
    public void addUser(UserData userData) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData(
            `username` String NOT NULL,
            `password` String NOT NULL,
            `email` String NOT NULL
            PRIMARY KEY (`username`)
            )
            """
    };

    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new Exception(); // EDIT THIS LATER
        }
    }
}
