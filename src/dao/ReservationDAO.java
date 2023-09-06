package dao;

import utils.ConsoleColors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import domain.Reservation;
import dao.BookDAO;

public class ReservationDAO {

    private static Connection connection;

    public ReservationDAO(Connection conn) {
        connection = conn;
    }

    public void checkOut(Reservation reservation) {
        String insertQuery = "INSERT INTO reservations (isbn,borrower_id,duration) VALUES (?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, reservation.getIsbn());
            preparedStatement.setInt(2, reservation.getBorrowerId());
            preparedStatement.setInt(3, reservation.getDuration());

            preparedStatement.executeUpdate();
            if(BookDAO.downgradeQuantity(reservation.getIsbn())){
                System.out.println(ConsoleColors.GREEN+"Reservation INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
            }else {
                System.out.println(ConsoleColors.RED+"No more copies for this book."+ConsoleColors.RESET);
            };

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert the Reservation into the database.");
        }
    }

    public void checkIn(){
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for search criteria
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        // Prompt the user for the new quantity
        System.out.print("Enter Borrower ID: ");
        int borrower_id = scanner.nextInt();

        // SQL query to update the quantity of the book
        String sql = "UPDATE reservations SET status = 'returned' WHERE ISBN = ? and borrower_id = ? and status = 'Borrowed'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(2, borrower_id);
            preparedStatement.setString(1, isbn);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                BookDAO.upgradeQuantity(isbn);
                System.out.println(ConsoleColors.GREEN+"the book checked In successfully."+ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED+"No Reservation whit this Informations."+ConsoleColors.RESET);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showAllReservations(){
        String sql = "SELECT reservations.* ,books.*,borrowers.*,authors.name as author FROM reservations join books on books.isbn = reservations.isbn join borrowers on borrowers.id = reservations.borrower_id join authors on books.author_id = authors.id;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Display the list of books in a formatted table
            System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %n",
                    "ISBN", "Title","Author","Name","Phone","borrowing_date","Duration","Status");
            System.out.println(
                    "-----------------------------------------------------------------------------");

            while (resultSet.next()) {
                // Retrieve book information from the result set
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");

                // Retrieve borrower information from the result set
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");

                // Retrieve reservation information from the result set
                Date borrowingDate = resultSet.getDate("borrowing_date");
                int duration = resultSet.getInt("duration");
                String status = resultSet.getString("status");

                // You can retrieve more columns as needed

                // Print the information in a formatted table
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %n",
                        isbn, title, author, name, phone, borrowingDate, duration, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
