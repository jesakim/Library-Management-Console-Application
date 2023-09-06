package services;

import dao.AuthorDAO;
import dao.BookDAO;
import domain.Author;
import utils.ConsoleColors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AuthorService {

    private AuthorDAO authorDAO;
    Scanner scanner = new Scanner(System.in);

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public void createAndSaveAuthor(){
        String name;
        do {
            System.out.print("Enter Author Name: ");
            name = scanner.nextLine();
        } while (!name.matches("\\S+"));

        Author author = new Author(name);

        if (authorDAO.insert(author)){
            System.out.println(ConsoleColors.GREEN+"Author INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
        }else{
            System.out.println(ConsoleColors.RED+"Author Failed."+ConsoleColors.RESET);
        }

    }

    public void showAllAuthors(){
        // Display the list of books in a formatted table
        System.out.printf("%-15s %-15s%n",
                "Id", "Name");
        System.out.println(
                "-----------------------------------------------------------------------------");

        ResultSet resultSet = authorDAO.getAllAuthors();
        try {
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
