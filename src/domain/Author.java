package domain;

import java.util.ArrayList;
import java.util.Scanner;


public class Author{
        Scanner scanner = new Scanner(System.in);
        private String name;

        public ArrayList<Book> books;

        // Constructor
        public Author() {
            do {
                System.out.print("Enter Author Name: ");
                this.name = scanner.nextLine();
            } while (!name.matches("\\S+"));
        }

        // Getter for name
        public String getName() {
            return name;
        }

        // Setter for name
        public void setName(String name) {
            this.name = name;
        }
}
