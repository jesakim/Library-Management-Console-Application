package domain;


import java.util.Scanner;

public class Borrower {
    Scanner scanner = new Scanner(System.in);
    private String name;
    private String phone;

    // Constructor
    public Borrower() {
        do {
            System.out.print("Enter Borrower Name: ");
            this.name = scanner.nextLine();
        } while (!name.matches("\\S+"));

        do {
            System.out.print("Enter Borrower Phone: ");
            this.phone = scanner.nextLine();
        } while (!phone.matches("\\S+"));
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for phone
    public String getPhone() {
        return phone;
    }

    // Setter for phone
    public void setPhone(String phone) {
        this.phone = phone;
    }

}