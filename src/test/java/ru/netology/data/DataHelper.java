package ru.netology.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "pass";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Получение последнего сгенерированного кода подтверждения для пользователя.
     * Таблица auth_codes в schema.sql содержит поля: id, user_id, code, created.
     */
    public static String getAuthCode(String login) {
        String sql = "SELECT code FROM auth_codes ac " +
                "JOIN users u ON ac.user_id = u.id " +
                "WHERE u.login = ? " +
                "ORDER BY ac.created DESC LIMIT 1";
        try (Connection conn = getConnection()) {
            QueryRunner runner = new QueryRunner();
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении кода из БД", e);
        }
    }

    /**
     * Полная очистка данных, чтобы SUT не падал при повторном запуске.
     * Порядок важен из-за внешних ключей.
     */
    public static void cleanDatabase() {
        try (Connection conn = getConnection()) {
            QueryRunner runner = new QueryRunner();
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке БД", e);
        }
    }

    /**
     * Данные для авторизации.
     */
    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}