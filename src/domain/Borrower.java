package domain;


import java.util.List;
import java.util.Scanner;

public class Borrower {
    Scanner scanner = new Scanner(System.in);
    private String name;
    private String phone;

    public List<Reservation> reservations;

    // Constructor
    public Borrower(String name,String phone) {
        this.name = name;
        this.phone = phone;
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