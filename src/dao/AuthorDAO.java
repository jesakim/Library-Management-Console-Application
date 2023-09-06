package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import domain.Author;
import utils.ConsoleColors;

public class AuthorDAO {
    private static Connection connection;

    public AuthorDAO(Connection conn) {
        connection = conn;
    }

    public boolean insert(Author author) {
        String insertQuery = "INSERT INTO authors (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, author.getName());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert the Author into the database.");
        }
        return false;
    }

    public ResultSet getAllAuthors(){
        // Define column widths (adjust as needed)



        String sql = "SELECT * FROM authors";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
