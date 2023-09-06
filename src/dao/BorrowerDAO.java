package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import domain.Borrower;

public class BorrowerDAO {

    private static Connection connection;

    public BorrowerDAO(Connection conn) {
        connection = conn;
    }

    public void insert(Borrower borrower) {
        String insertQuery = "INSERT INTO borrowers (name,phone) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, borrower.getName());
            preparedStatement.setString(2, borrower.getPhone());

            preparedStatement.executeUpdate();
            System.out.println(ConsoleColors.GREEN+"Borrower INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert the Borrower into the database.");
        }
    }

    public void showAllBorrowers(){
            // Define column widths (adjust as needed)



            String sql = "SELECT * FROM borrowers";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                // Display the list of books in a formatted table
                System.out.printf("%-15s %-15s %-15s %n",
                        "Id", "Name","Phone");
                System.out.println(
                        "-----------------------------------------------------------------------------");

                while (resultSet.next()) {
                    // Retrieve book information from the result set
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");

                    System.out.printf("%-15s %-15s %-15s %n",
                            id,name,phone);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public static Boolean checkBorrowerId(int id) {
        String sql = "SELECT COUNT(*) FROM borrowers WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
