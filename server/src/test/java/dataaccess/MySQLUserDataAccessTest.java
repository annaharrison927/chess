package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class MySQLUserDataAccessTest {

    private static MySQLUserDataAccess userDataAccess;

    @BeforeAll
    public static void init() throws DataAccessException {
        userDataAccess = new MySQLUserDataAccess();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDataAccess.clear();
    }

    @Test
    void addUserGood() {
        UserData goodUserData = new UserData("Elton John", "TinyDancer01", "rocketman@gmail.com");
        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.addUser(goodUserData);
        });
    }

    @Test
    void addUserBad() {
        UserData badUserData = new UserData("Granny", null, "istillusepaper@aol.com");
        Assertions.assertThrows(DataAccessException.class, () -> userDataAccess.addUser(badUserData));
    }

    @Test
    void getUserGood() throws DataAccessException {
        UserData goodUserData = new UserData("Elton John", "TinyDancer01", "rocketman@gmail.com");
        userDataAccess.addUser(goodUserData);

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.getUser("Elton John");
        });
    }

    @Test
    void getUserBad() throws DataAccessException {
        Assertions.assertNull(userDataAccess.getUser("Granny"));
    }

    @Test
    void clear() throws DataAccessException {
        UserData goodUserData = new UserData("Elton John", "TinyDancer01", "rocketman@gmail.com");
        userDataAccess.addUser(goodUserData);

        Assertions.assertDoesNotThrow(() -> {
            userDataAccess.clear();
        });
    }
}