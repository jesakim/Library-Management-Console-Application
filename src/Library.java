import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import dao.*;
import domain.*;
import utils.ConsoleColors;
import utils.db.DatabaseConnection;

public class Library {

    public static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }

        BookDAO bookDAO = new BookDAO(connection);
        AuthorDAO authorDAO = new AuthorDAO(connection);
        BorrowerDAO borrowerDAO = new BorrowerDAO(connection);
        ReservationDAO reservationDAO = new ReservationDAO(connection);
        int choice;
        Scanner input = new Scanner(System.in);
        do {


            showMenu();


            while (!input.hasNextInt()) {
                System.out.println(ConsoleColors.RED+"ENTER A VALID CHOICE BETWEEN 0 AND 8."+ConsoleColors.RESET);
                input.next(); // Consume the non-integer input
            }

            choice = input.nextInt();
            switch (choice) {

                // Case
                case 0:
                    break;
                case 1:
                    Book book = new Book();
                    bookDAO.insert(book);
                    break;
                case 2:
                    bookDAO.upgradeQuantity();
                    break;
                case 3:
                    bookDAO.searchBooks();
                    break;
                case 4:
                    bookDAO.showAllBooks();
                    break;
                case 5:
                    Borrower borrower = new Borrower();
                    borrowerDAO.insert(borrower);
                    break;
                case 6:
                    borrowerDAO.showAllBorrowers();
                    break;
                case 7:
                    Reservation reservation = new Reservation();
                    reservationDAO.checkOut(reservation);
                    break;
                case 8:
                    reservationDAO.checkIn();
                    break;

                case 9:
                    Author author = new Author();
                    authorDAO.insert(author);
                    break;
                case 10:
                    authorDAO.showAllAuthors();
                    break;
                case 11:
                    reservationDAO.showAllReservations();
                    break;
                case 12:
                    bookDAO.deleteBook();
                    break;
                case 13:
                    Book bookToBeUpdated =  bookDAO.getBookByISBN();
                    bookDAO.updateBook(bookToBeUpdated);
                    break;
                case 14:
                    generateStatisticsReport();
                    break;
                default:
                    System.out.println(ConsoleColors.RED+"ENTER A VALID CHOICE BETWEEN 0 AND 8."+ConsoleColors.RESET);
            }
        }while (choice != 0);
    }

    public static void showMenu(){
        // Displaying menu
        System.out.println(
                "----------------------------------------------------------------------------------------------------------");
        System.out.println("Press 1 to Add new Book.");
        System.out.println(
                "Press 2 to Upgrade Quantity of a Book.");
        System.out.println("Press 3 to Search a Book.");
        System.out.println("Press 4 to Show All Books.");
        System.out.println("Press 5 to Register Borrower.");
        System.out.println(
                "Press 6 to Show All Registered Borrowers.");
        System.out.println("Press 7 to Check Out Book. ");
        System.out.println("Press 8 to Check In Book");
        System.out.println("Press 9 Add Author");
        System.out.println("Press 10 Show All Authors");
        System.out.println("Press 11 Show All Reservations");
        System.out.println(ConsoleColors.RED+"Press 12 Delete Book"+ConsoleColors.RESET);
        System.out.println("Press 13 Update Book");
        System.out.println("Press 14 Generate Statistics Report");
        System.out.println("Press 0 to Exit Application.");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
    }

    public static void generateStatisticsReport() {
        Connection connection;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }

        String sql = "SELECT COUNT(*) as borrowed," +
                "(SELECT COUNT(*) FROM `reservations` WHERE status = 'Lost') as lost," +
                "(SELECT COUNT(*) FROM `books`) as allbooks," +
                "(SELECT COUNT(*) FROM `books` WHERE books.quantity > 0) as availablebooks," +
                "(SELECT COUNT(*) FROM `reservations` WHERE status = 'Returned') as returned," +
                "(SELECT COUNT(*) FROM `authors`) as authors," +
                "(SELECT COUNT(*) FROM `borrowers`) as borrowers " +
                "FROM `reservations` WHERE status = 'Borrowed';";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Print the result
            while (resultSet.next()) {
                int borrowed = resultSet.getInt("borrowed");
                int lost = resultSet.getInt("lost");
                int allbooks = resultSet.getInt("allbooks");
                int availablebooks = resultSet.getInt("availablebooks");
                int returned = resultSet.getInt("returned");
                int authors = resultSet.getInt("authors");
                int borrowers = resultSet.getInt("borrowers");

                System.out.println("Statistics Report -----------------------");
                System.out.println("Borrowed Books: " + ConsoleColors.GREEN + borrowed + ConsoleColors.RESET);
                System.out.println("Returned Books: " + ConsoleColors.GREEN + returned + ConsoleColors.RESET);
                System.out.println("Lost Books: " + ConsoleColors.GREEN + lost + ConsoleColors.RESET);
                System.out.println("Total Books: " + ConsoleColors.GREEN + allbooks + ConsoleColors.RESET);
                System.out.println("Available Books: " + ConsoleColors.GREEN + availablebooks + ConsoleColors.RESET);
                System.out.println("Total Authors: " + ConsoleColors.GREEN + authors + ConsoleColors.RESET);
                System.out.println("Total Borrowers: " + ConsoleColors.GREEN + borrowers + ConsoleColors.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
