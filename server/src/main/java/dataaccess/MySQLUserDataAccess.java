package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLUserDataAccess implements UserDataAccess {
    @Override
    public void addUser(UserData userData) throws SQLException, DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertUser(connection, userData.username(), userData.password(), userData.email());
        }
    }

    @Override
    public UserData getUser(String username) throws SQLException, DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM userData WHERE id=?";

        }
    }

    @Override
    public void clear() {

    }

    private void insertUser(Connection conn, String username, String password, String email) throws SQLException {
        try (var preparedStatement = conn.prepareStatement(
                "INSERT INTO userData (username, password, email) VALUES (?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();
        }
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
