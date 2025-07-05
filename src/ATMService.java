import java.sql.*;
import java.util.*;

public class ATMService {
    public static void atmServices(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        String acc = sc.nextLine();
        System.out.print("Enter PIN (or type 'set' to setup new PIN): ");
        String pin = sc.nextLine();

        if (pin.equalsIgnoreCase("set")) {
            System.out.print("Set 4-digit PIN: ");
            String newPin = sc.nextLine();
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET pin=? WHERE account_number=?");
            ps.setString(1, newPin);
            ps.setString(2, acc);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                System.out.println("PIN set successfully.");
            } else {
                System.out.println("Account not found. PIN not set.");
            }
            return;
        }

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE account_number=? AND pin=?");
        ps.setString(1, acc);
        ps.setString(2, pin);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            System.out.println("Invalid account number or PIN");
            return;
        }

        int balance = rs.getInt("age");

        // Ensure 'transactions' column exists in users table
        Statement stmtInit = conn.createStatement();
        stmtInit.executeUpdate("ALTER TABLE users ADD COLUMN transactions TEXT DEFAULT ''");

        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance Enquiry");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int opt = sc.nextInt();

            StringBuilder historyEntry = new StringBuilder();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            switch (opt) {
                case 1:
                    System.out.print("Deposit amount: ");
                    int dep = sc.nextInt();
                    balance += dep;
                    historyEntry.append(now + " | Deposit | Rs. " + dep + "\n");
                    System.out.println("Deposited Rs. " + dep);
                    break;
                case 2:
                    System.out.print("Withdraw amount: ");
                    int wd = sc.nextInt();
                    if (wd > balance) {
                        System.out.println("Insufficient funds");
                    } else {
                        balance -= wd;
                        historyEntry.append(now + " | Withdraw | Rs. " + wd + "\n");
                        System.out.println("Withdrawn Rs. " + wd);
                    }
                    break;
                case 3:
                    System.out.println("Balance: Rs. " + balance);
                    break;
                case 4:
                    PreparedStatement viewStmt = conn.prepareStatement("SELECT transactions FROM users WHERE account_number=?");
                    viewStmt.setString(1, acc);
                    ResultSet transRs = viewStmt.executeQuery();
                    System.out.println("\n--- Transaction History ---");
                    if (transRs.next()) {
                        String history = transRs.getString("transactions");
                        if (history == null || history.isEmpty()) {
                            System.out.println("No transactions available.");
                        } else {
                            System.out.println(history);
                        }
                    }
                    break;
                case 5:
                    // Save balance
                    PreparedStatement ups = conn.prepareStatement("UPDATE users SET age=?, transactions=CONCAT(IFNULL(transactions,''), ?) WHERE account_number=?");
                    ups.setInt(1, balance);
                    ups.setString(2, historyEntry.toString());
                    ups.setString(3, acc);
                    ups.executeUpdate();
                    return;
                default:
                    System.out.println("Invalid option");
            }

            if (!historyEntry.toString().isEmpty()) {
                PreparedStatement updateTrans = conn.prepareStatement("UPDATE users SET transactions=CONCAT(IFNULL(transactions,''), ?) WHERE account_number=?");
                updateTrans.setString(1, historyEntry.toString());
                updateTrans.setString(2, acc);
                updateTrans.executeUpdate();
            }
        }
    }
}