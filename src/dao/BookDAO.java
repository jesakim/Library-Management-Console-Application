package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import domain.Book;

public class BookDAO {
    private int width = 15;
    private static Connection connection;

    public BookDAO(Connection conn) {
        connection = conn;
    }

    public void insert(Book book) {
        String insertQuery = "INSERT INTO books (isbn, title, author_id, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setInt(3, book.getAuthorId());
            preparedStatement.setInt(4, book.getQuantity());

            preparedStatement.executeUpdate();
            System.out.println(ConsoleColors.GREEN+"BOOK INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert the book into the database.");
        }
    }


    public void showAllBooks(){
        // Define column widths (adjust as needed)



        String sql = "SELECT * FROM library.books join authors on books.author_id = authors.id;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Display the list of books in a formatted table
            System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "s %-" + width + "s%n",
                    "ISBN", "Title", "Author", "Quantity");
            System.out.println(
                    "-----------------------------------------------------------------------------");

            while (resultSet.next()) {
                // Retrieve book information from the result set
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("title");
                String authorId = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");

                System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "s %-" + width + "d%n",
                        isbn, title, authorId, quantity);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchBooks(){

        Scanner scanner = new Scanner(System.in);

        // Prompt the user for search criteria
        System.out.print("Enter ISBN or title to search: ");
        String searchCriteria = scanner.nextLine();

        String sql = "SELECT ISBN, title, author_id, quantity FROM books WHERE ISBN LIKE ? OR title LIKE ?";

        System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "s %-" + width + "s%n",
                "ISBN", "Title", "Author ID", "Quantity");
        System.out.println(
                "-----------------------------------------------------------------------------");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for the search
            preparedStatement.setString(1, "%" + searchCriteria + "%");
            preparedStatement.setString(2, "%" + searchCriteria + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve book information from the result set
                    String isbn = resultSet.getString("ISBN");
                    String title = resultSet.getString("title");
                    int authorId = resultSet.getInt("author_id");
                    int quantity = resultSet.getInt("quantity");

                    System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "d %-" + width + "d%n",
                            isbn, title, authorId, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upgradeQuantity(){
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for search criteria
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        if (!checkISBN(isbn)) {
            System.out.println(ConsoleColors.RED+"Book with ISBN " + isbn + " does not exist."+ConsoleColors.RESET);
            return;
        }

        // Prompt the user for the new quantity
        System.out.print("Enter the quantity: ");
        int quantity = scanner.nextInt();

        // SQL query to update the quantity of the book
        String sql = "UPDATE books SET quantity = quantity + ? WHERE ISBN = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, isbn);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ConsoleColors.GREEN+"Quantity updated successfully."+ConsoleColors.RESET);
            } else {
                System.out.println("Failed to update quantity.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void upgradeQuantity(String isbn){

        // SQL query to update the quantity of the book
        String sql = "UPDATE books SET quantity = quantity + 1 WHERE ISBN = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean downgradeQuantity(String isbn){
        String sql = "UPDATE books SET quantity = quantity - 1 WHERE ISBN = ? and quantity >=1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public Book getBookByISBN() {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for search criteria
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        while(!checkISBN(isbn)) {
            System.out.println(ConsoleColors.RED+"Book with ISBN " + isbn + " does not exist."+ConsoleColors.RESET);
            System.out.print("Enter ISBN: ");
            isbn = scanner.nextLine();
        }
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "s %-" + width + "s%n",
                    "ISBN", "Title", "Author ID", "Quantity");
            System.out.println(
                    "-----------------------------------------------------------------------------");

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int quantity = resultSet.getInt("quantity");

                System.out.printf("%-" + width + "s %-" + width + "s %-" + width + "d %-" + width + "d%n",
                        isbn, title, authorId, quantity);

                // Create and return a Book object
                return new Book(isbn, title, authorId, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to update book information by ISBN
    public void updateBook(Book updatedBook) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter new title: ");
        String newTitle = scanner.nextLine();

        System.out.print("Enter new author ID: ");
        int newAuthorId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE books SET title = ?, author_id = ?, quantity = ? WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setInt(2, newAuthorId);
            preparedStatement.setInt(3, newQuantity);
            preparedStatement.setString(4, updatedBook.getIsbn());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(ConsoleColors.GREEN+"Quantity updated successfully."+ConsoleColors.RESET);
            } else {
                System.out.println("Failed to update quantity.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Boolean checkISBN(String ISBN) {
        String sql = "SELECT COUNT(*) FROM books WHERE ISBN = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ISBN);

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
    public boolean deleteBook() {

        Scanner scanner = new Scanner(System.in);

        // Prompt the user for search criteria
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        String deleteSQL = "UPDATE books SET quantity = 0 WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, isbn);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book with ISBN " + isbn + " has been deleted.");
                return true;
            } else {
                System.out.println("Book with ISBN " + isbn + " not found in the database.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
}
