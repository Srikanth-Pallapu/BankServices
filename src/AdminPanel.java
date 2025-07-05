import java.sql.*;
import java.util.*;

public class AdminPanel {
    public static void adminPanel(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Admin Access ---");
        System.out.println("1. Login");
        System.out.println("2. Signup (Create Admin Account)");
        System.out.print("Choose option: ");
        int choice = sc.nextInt(); sc.nextLine();

        if (choice == 2) {
            System.out.print("Create Admin Username: ");
            String newUser = sc.nextLine();
            System.out.print("Create Admin Password: ");
            String newPass = sc.nextLine();

            PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM admin WHERE username=?");
            checkUser.setString(1, newUser);
            ResultSet rsCheck = checkUser.executeQuery();

            if (rsCheck.next()) {
                System.out.println("Admin username already exists. Please login.");
            } else {
                PreparedStatement insertAdmin = conn.prepareStatement("INSERT INTO admin (username, password) VALUES (?, ?)");
                insertAdmin.setString(1, newUser);
                insertAdmin.setString(2, newPass);
                insertAdmin.executeUpdate();
                System.out.println("Admin registered successfully!");
            }
        }

        // Admin login
        System.out.println("\n--- Admin Login ---");
        System.out.print("Enter Admin Username: ");
        String user = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin WHERE username=? AND password=?");
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            System.out.println("Invalid admin credentials. Access denied.");
            return;
        }

        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View All Users");
            System.out.println("2. Update User Info");
            System.out.println("3. Delete User");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int ch = sc.nextInt(); sc.nextLine();
            switch (ch) {
                case 1:
                    Statement stmt = conn.createStatement();
                    ResultSet users = stmt.executeQuery("SELECT * FROM users");
                    while (users.next()) {
                        System.out.println("Account: " + users.getString("account_number") + ", Name: " + users.getString("name"));
                    }
                    break;
                case 2:
                    System.out.print("Account Number to update: ");
                    String upAcc = sc.nextLine();
                    System.out.print("New Phone: ");
                    String newPhone = sc.nextLine();
                    PreparedStatement ups = conn.prepareStatement("UPDATE users SET phone=? WHERE account_number=?");
                    ups.setString(1, newPhone);
                    ups.setString(2, upAcc);
                    ups.executeUpdate();
                    System.out.println("User updated.");
                    break;
                case 3:
                    System.out.print("Account Number to delete: ");
                    String delAcc = sc.nextLine();
                    PreparedStatement del = conn.prepareStatement("DELETE FROM users WHERE account_number=?");
                    del.setString(1, delAcc);
                    del.executeUpdate();
                    System.out.println("User deleted.");
                    break;
                case 4: return;
                default: System.out.println("Invalid option");
            }
        }
    }
}