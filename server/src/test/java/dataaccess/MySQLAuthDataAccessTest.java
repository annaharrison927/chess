package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MySQLAuthDataAccessTest {

    private static MySQLAuthDataAccess authDataAccess;

    @BeforeAll
    public static void init() throws DataAccessException {
        authDataAccess = new MySQLAuthDataAccess();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDataAccess.clear();
    }

    @Test
    void addAuthGood() {
        AuthData goodAuthData = new AuthData("fdq93htrkuaehr92uyrohcuqh93hro", "Elton John");
        Assertions.assertDoesNotThrow(() -> {
            authDataAccess.addAuth(goodAuthData);
        });
    }

    @Test
    void addAuthBad() {
        AuthData badAuthData = new AuthData(null, "Granny");
        Assertions.assertThrows(DataAccessException.class, () -> authDataAccess.addAuth(badAuthData));
    }

    @Test
    void getAuthGood() throws DataAccessException {
        AuthData goodAuthData = new AuthData("fdq93htrkuaehr92uyrohcuqh93hro", "Elton John");
        authDataAccess.addAuth(goodAuthData);

        Assertions.assertDoesNotThrow(() -> {
            authDataAccess.getAuth("fdq93htrkuaehr92uyrohcuqh93hro");
        });
    }

    @Test
    void getAuthBad() throws DataAccessException {
        AuthData goodAuthData = new AuthData("fdq93htrkuaehr92uyrohcuqh93hro", "Elton John");
        authDataAccess.addAuth(goodAuthData);

        Assertions.assertNull(authDataAccess.getAuth("dakjdhuiewrhi328"));
    }

    @Test
    void deleteAuthGood() throws DataAccessException {
        AuthData goodAuthData = new AuthData("fdq93htrkuaehr92uyrohcuqh93hro", "Elton John");
        authDataAccess.addAuth(goodAuthData);

        Assertions.assertDoesNotThrow(() -> {
            authDataAccess.deleteAuth("fdq93htrkuaehr92uyrohcuqh93hro");
        });
    }

    @Test
    void deleteAuthBad() {
        AuthData badAuthData = new AuthData(null, "Granny");

        Assertions.assertThrows(DataAccessException.class, () -> authDataAccess.addAuth(badAuthData));
    }

    @Test
    void clear() throws DataAccessException {
        AuthData goodAuthData = new AuthData("fdq93htrkuaehr92uyrohcuqh93hro", "Elton John");
        authDataAccess.addAuth(goodAuthData);

        AuthData batmanAuthData = new AuthData("nanananananananananananana", "BATMAN");
        authDataAccess.addAuth(batmanAuthData);

        Assertions.assertDoesNotThrow(() -> {
            authDataAccess.clear();
        });
    }
}