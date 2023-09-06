package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import domain.Author;

public class AuthorDAO {
    private static Connection connection;

    public AuthorDAO(Connection conn) {
        connection = conn;
    }

    public void insert(Author author) {
        String insertQuery = "INSERT INTO authors (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, author.getName());

            preparedStatement.executeUpdate();
            System.out.println(ConsoleColors.GREEN+"Author INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert the Author into the database.");
        }
    }

    public void showAllAuthors(){
        // Define column widths (adjust as needed)



        String sql = "SELECT * FROM authors";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Display the list of books in a formatted table
            System.out.printf("%-15s %-15s%n",
                    "Id", "Name");
            System.out.println(
                    "-----------------------------------------------------------------------------");

            while (resultSet.next()) {
                // Retrieve book information from the result set
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("name");

                System.out.printf("%-15s %-15s%n",
                        id,name);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
