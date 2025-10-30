package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.*;

public class MySQLUserDataAccess implements UserDataAccess {

    public MySQLUserDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertUser(connection, userData.username(), userData.password(), userData.email());
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex); // EDIT THIS LATER
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return retrieveUser(connection, username);
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex); // EDIT THIS LATER
        }
    }

    @Override
    public void clear() {

    }

    private void insertUser(Connection conn, String username, String password, String email) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement(
                "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex); // EDIT THIS LATER
        }
    }

    private UserData retrieveUser(Connection conn, String username) throws DataAccessException {
        UserData userData = new UserData(username, null, null);
        try (var preparedStatement = conn.prepareStatement(
                "SELECT username, json FROM userData WHERE username=?")) {
            preparedStatement.setString(1, username);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString(("email"));

                    userData = new UserData(username, password, email);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex); // EDIT THIS LATER
        }
        return userData;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData(
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL
            PRIMARY KEY (`username`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }
}
