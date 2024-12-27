import java.io.*;
import java.util.*;

public class StudentManagementSystem {

    // Student class represents a student object
    static class Student {
        String id;
        String name;
        String grade;

        public Student(String id, String name, String grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Grade: " + grade;
        }

        // Method to update student details
        public void update(String name, String grade) {
            this.name = name;
            this.grade = grade;
        }
    }

    // StudentManager class manages the collection of students
    static class StudentManager {
        List<Student> students = new ArrayList<>();
        String filePath = "students.txt"; // File for storing student data

        // Load students from file
        public void loadStudents() {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        String id = data[0].trim();
                        String name = data[1].trim();
                        String grade = data[2].trim();
                        students.add(new Student(id, name, grade));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading students: " + e.getMessage());
            }
        }

        // Save students to file
        public void saveStudents() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Student student : students) {
                    writer.write(student.id + "," + student.name + "," + student.grade);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving students: " + e.getMessage());
            }
        }

        // Add a new student
        public void addStudent(String id, String name, String grade) {
            students.add(new Student(id, name, grade));
            saveStudents();
        }

        // Search for a student by ID
        public Student searchById(String id) {
            for (Student student : students) {
                if (student.id.equals(id)) {
                    return student;
                }
            }
            return null;
        }

        // List all students
        public void listStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println(student);
                }
            }
        }

        // Delete a student by ID
        public boolean deleteStudentById(String id) {
            Iterator<Student> iterator = students.iterator();
            while (iterator.hasNext()) {
                Student student = iterator.next();
                if (student.id.equals(id)) {
                    iterator.remove();
                    saveStudents();
                    return true;
                }
            }
            return false;
        }

        // Update a student's information by ID
        public boolean updateStudentById(String id, String name, String grade) {
            Student student = searchById(id);
            if (student != null) {
                student.update(name, grade);
                saveStudents();
                return true;
            }
            return false;
        }
    }

    // Main class to run the Student Management System
    public static class StudentApp {
        static Scanner scanner = new Scanner(System.in);
        static StudentManager studentManager = new StudentManager();

        public static void main(String[] args) {
            studentManager.loadStudents(); // Load students from file

            while (true) {
                displayMenu();
                int choice = getChoice();
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        searchStudent();
                        break;
                    case 3:
                        listStudents();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        }

        // Display menu options
        private static void displayMenu() {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add a new student");
            System.out.println("2. Search for a student by ID");
            System.out.println("3. List all students");
            System.out.println("4. Update student details");
            System.out.println("5. Delete a student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
        }

        // Get user input for the menu choice
        private static int getChoice() {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                return -1; // Return an invalid choice
            }
        }

        // Add a new student
        private static void addStudent() {
            System.out.print("Enter student ID: ");
            String id = scanner.nextLine();

            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter student grade: ");
            String grade = scanner.nextLine();

            studentManager.addStudent(id, name, grade);
            System.out.println("Student added successfully!");
        }

        // Search for a student by ID
        private static void searchStudent() {
            System.out.print("Enter student ID to search: ");
            String id = scanner.nextLine();
            Student student = studentManager.searchById(id);

            if (student != null) {
                System.out.println("Student found: " + student);
            } else {
                System.out.println("No student found with ID: " + id);
            }
        }

        // List all students
        private static void listStudents() {
            studentManager.listStudents();
        }

        // Update student details
        private static void updateStudent() {
            System.out.print("Enter student ID to update: ");
            String id = scanner.nextLine();
            Student student = studentManager.searchById(id);

            if (student != null) {
                System.out.println("Current details: " + student);

                System.out.print("Enter new name: ");
                String name = scanner.nextLine();

                System.out.print("Enter new grade: ");
                String grade = scanner.nextLine();

                if (studentManager.updateStudentById(id, name, grade)) {
                    System.out.println("Student updated successfully!");
                } else {
                    System.out.println("Failed to update student.");
                }
            } else {
                System.out.println("No student found with ID: " + id);
            }
        }

        // Delete a student by ID
        private static void deleteStudent() {
            System.out.print("Enter student ID to delete: ");
            String id = scanner.nextLine();

            if (studentManager.deleteStudentById(id)) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with ID: " + id);
            }
        }
    }
}
