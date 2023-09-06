package domain;


import java.util.Scanner;

public class Reservation {
        Scanner scanner = new Scanner(System.in);
        private String isbn;
        private int borrowerId;
        private int duration;

        // Constructor
        public Reservation() {
            do {
                System.out.print("Enter the book ISBN: ");
                this.isbn = scanner.nextLine();
            }while (!BookDAO.checkISBN(this.isbn));

            do {
                System.out.print("Enter Borrower ID: ");
                this.borrowerId = scanner.nextInt();
            }while (!BorrowerDAO.checkBorrowerId(this.borrowerId));

            System.out.print("Enter duration: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Enter duration: ");
                scanner.next();
            }
            this.duration = scanner.nextInt();
        }

        // Getter for ISBN
        public String getIsbn() {
            return isbn;
        }

        // Setter for ISBN
        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        // Getter for Borrower ID
        public int getBorrowerId() {
            return borrowerId;
        }

        // Setter for Borrower ID
        public void setBorrowerId(int borrowerId) {
            this.borrowerId = borrowerId;
        }

        // Getter for Duration
        public int getDuration() {
            return duration;
        }

        // Setter for Duration
        public void setDuration(int duration) {
            this.duration = duration;
        }
}
