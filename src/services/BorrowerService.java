package services;

import dao.BorrowerDAO;
import dao.ReservationDAO;
import domain.Borrower;
import utils.ConsoleColors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BorrowerService {

    private int width = 15;

    private BorrowerDAO borrowerDAO;
    Scanner scanner = new Scanner(System.in);

    public BorrowerService(BorrowerDAO borrowerDAO) {
        this.borrowerDAO = borrowerDAO;
    }

    public void insert(){
        String name;
        do {
            System.out.print("Enter Borrower Name: ");
            name = scanner.nextLine();
        }while (!name.matches("\\S+"));

        String phone;
        do {
            System.out.print("Enter Borrower Phone: ");
            phone = scanner.nextLine();
        } while (!phone.matches("\\S+"));

        Borrower borrower = new Borrower(name,phone);

        if (borrowerDAO.insert(borrower)){
            System.out.println(ConsoleColors.GREEN+"Borrower INSERTED SUCCESSFULLY."+ConsoleColors.RESET);
        }

    }

    public void showAllBorrowers(){
        // Display the list of books in a formatted table
        System.out.printf("%-15s %-15s %-15s %n",
                "Id", "Name","Phone");
        System.out.println(
                "-----------------------------------------------------------------------------");

        ResultSet resultSet = borrowerDAO.getAllBorrowers();

        try {
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
}
