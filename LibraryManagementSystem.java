import java.io.*;
import java.util.*;

public class LibraryManagementSystem {

    // Book class represents a book object
    static class Book {
        String title;
        String author;
        String isbn;
        int year;

        public Book(String title, String author, String isbn, int year) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.year = year;
        }

        @Override
        public String toString() {
            return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Year: " + year;
        }
    }

    // Library class represents the collection of books
    static class Library {
        List<Book> books = new ArrayList<>();
        String filePath = "books.txt";  // File to store books

        // Load books from the file
        public void loadBooks() {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 4) {
                        String title = data[0].trim();
                        String author = data[1].trim();
                        String isbn = data[2].trim();
                        int year = Integer.parseInt(data[3].trim());
                        books.add(new Book(title, author, isbn, year));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }
        }

        // Save books to the file
        public void saveBooks() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Book book : books) {
                    writer.write(book.title + "," + book.author + "," + book.isbn + "," + book.year);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to the file: " + e.getMessage());
            }
        }

        // Add a new book
        public void addBook(String title, String author, String isbn, int year) {
            books.add(new Book(title, author, isbn, year));
            saveBooks();
        }

        // Search for a book by ISBN
        public Book searchBookByIsbn(String isbn) {
            for (Book book : books) {
                if (book.isbn.equals(isbn)) {
                    return book;
                }
            }
            return null;
        }

        // List all books
        public void listBooks() {
            if (books.isEmpty()) {
                System.out.println("No books available in the library.");
            } else {
                for (Book book : books) {
                    System.out.println(book);
                }
            }
        }
    }

    // Main class to interact with the user
    public static class LibraryApp {
        static Scanner scanner = new Scanner(System.in);
        static Library library = new Library();

        public static void main(String[] args) {
            library.loadBooks(); // Load books from the file

            while (true) {
                displayMenu();
                int choice = getChoice();
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        searchBook();
                        break;
                    case 3:
                        listBooks();
                        break;
                    case 4:
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        }

        // Display menu options
        private static void displayMenu() {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add a new book");
            System.out.println("2. Search for a book by ISBN");
            System.out.println("3. List all books");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
        }

        // Get user input for the menu
        private static int getChoice() {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                return -1; // Return an invalid choice
            }
        }

        // Add a new book to the library
        private static void addBook() {
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();

            System.out.print("Enter book author: ");
            String author = scanner.nextLine();

            System.out.print("Enter book ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("Enter publication year: ");
            int year = Integer.parseInt(scanner.nextLine());

            library.addBook(title, author, isbn, year);
            System.out.println("Book added successfully!");
        }

        // Search for a book by ISBN
        private static void searchBook() {
            System.out.print("Enter ISBN of the book to search: ");
            String isbn = scanner.nextLine();
            Book book = library.searchBookByIsbn(isbn);

            if (book != null) {
                System.out.println("Book found: " + book);
            } else {
                System.out.println("Book not found with ISBN: " + isbn);
            }
        }

        // List all books in the library
        private static void listBooks() {
            library.listBooks();
        }
    }
}
