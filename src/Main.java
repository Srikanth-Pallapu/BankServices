import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Bank Management System ---");
            System.out.println("1. Register User");
            System.out.println("2. ATM Services");
            System.out.println("3. Admin Panel");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            try {
                switch (choice) {
                    case 1: UserRegistration.registerUser(conn); break;
                    case 2: ATMService.atmServices(conn); break;
                    case 3: AdminPanel.adminPanel(conn); break;
                    case 4: System.exit(0);
                    default: System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}