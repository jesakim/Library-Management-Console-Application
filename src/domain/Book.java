package domain;


import java.util.Scanner;
import dao.BookDAO;

public class Book {
    Scanner scanner = new Scanner(System.in);
    private String ISBN;
    private String title;
    private int author_id;
    private int quantity;

    public Book(String isbn, int quantity) {
        this.ISBN = isbn;
        this.quantity = quantity;
    }

    public Book(String isbn, String title, int author_id, int quantity) {
        this.ISBN = isbn;
        this.title = title;
        this.author_id = author_id;
        this.quantity = quantity;
    }

    // Getter and Setter methods for ISBN
    public String getIsbn() {
        return ISBN;
    }

    public void setIsbn(String isbn) {
        this.ISBN = isbn;
    }

    // Getter and Setter methods for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter methods for author_id
    public int getAuthorId() {
        return author_id;
    }

    public void setAuthorId(int author_id) {
        this.author_id = author_id;
    }

    // Getter and Setter methods for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}