package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users ("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "username VARCHAR(255),"
                    + "phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            try (var statement2 = conn.createStatement()) {
                statement2.executeUpdate(sql2);
            }

            var sql4 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "33333333");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "44444444");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Bob");
                preparedStatement.setString(2, "555555");
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }

                preparedStatement.setString(1, "Helen");
                preparedStatement.setString(2, "6666666");
                preparedStatement.executeUpdate();
            }

            var sql5 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql5, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Sarah");
                preparedStatement.setString(2, "333333333");
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }
            var sql6 = "DELETE FROM users WHERE username = 'Maria'";
            try (var preparedStatement = conn.prepareStatement(sql6)) {
                preparedStatement.executeLargeUpdate();
            }
            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}
