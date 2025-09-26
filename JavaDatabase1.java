import java.sql.*;
import java.util.Scanner;

public class JavaDatabase1 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/university_db2";
        String user = "root";
        String password = "Root123;";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Choose an option:");
            System.out.println("1 - Register");
            System.out.println("2 - Login");
            int choice = scanner.nextInt();
            scanner.nextLine(); //consume leftover newline

            switch (choice) {
                case 1:
                    registerUser(connection, scanner);
                    break;
                case 2:
                    if (loginUser(connection, scanner)) {
                        System.out.println("Login successful!");
                        System.out.println("Would you like to:");
                        System.out.println("1 - Update your details");
                        System.out.println("2 - Delete your account");
                        System.out.println("3 - Exit");
                        int postLoginChoice = scanner.nextInt();
                        scanner.nextLine(); //consume newline

                        switch (postLoginChoice) {
                            case 1:
                                updateUser(connection, scanner);
                                break;
                            case 2:
                                deleteUser(connection, scanner);
                                break;
                            default:
                                System.out.println("Goodbye!");
                        }
                    } else {
                        System.out.println("Login failed. Invalid credentials.");
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            scanner.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void registerUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter your student id: ");
        int student_id = scanner.nextInt();
        scanner.nextLine(); //consume newline

        System.out.print("Enter your first name: ");
        String first_name = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String last_name = scanner.nextLine();

        String sql = "INSERT INTO students (student_id, first_name, last_name) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, student_id);
        ps.setString(2, first_name);
        ps.setString(3, last_name);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }

        ps.close();
    }

    private static boolean loginUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter your student id: ");
        int student_id = scanner.nextInt();
        scanner.nextLine(); //consume newline

        System.out.print("Enter your first name: ");
        String first_name = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String last_name = scanner.nextLine();

        String sql = "SELECT * FROM students WHERE student_id = ? AND first_name = ? AND last_name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, student_id);
        ps.setString(2, first_name);
        ps.setString(3, last_name);

        ResultSet rs = ps.executeQuery();

        boolean authenticated = rs.next();

        rs.close();
        ps.close();
        return authenticated;
    }

    private static void updateUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter your student id: ");
        int student_id = scanner.nextInt();
        scanner.nextLine(); //consume newline

        System.out.print("Enter your new first name: ");
        String first_name = scanner.nextLine();

        System.out.print("Enter your new last name: ");
        String last_name = scanner.nextLine();

        String sql = "UPDATE students SET first_name = ?, last_name = ? WHERE student_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, first_name);
        ps.setString(2, last_name);
        ps.setInt(3, student_id);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Details updated successfully.");
        } else {
            System.out.println("Update failed. Check your student ID.");
        }

        ps.close();
    }

    private static void deleteUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter your student id to confirm deletion: ");
        int student_id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String sql = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, student_id);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Delete failed. Check your student ID.");
        }

        ps.close();
    }
}
