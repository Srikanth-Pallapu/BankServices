import java.sql.*;
import java.util.*;

public class UserRegistration {
    public static void registerUser(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Father's Name: "); String father = sc.nextLine();
        System.out.print("Age: "); int age = sc.nextInt(); sc.nextLine();
        System.out.print("Phone: "); String phone = sc.nextLine();
        System.out.print("Address: "); String address = sc.nextLine();
        System.out.print("Aadhar ID: "); String aadhar = sc.nextLine();
        System.out.print("Gender: "); String gender = sc.nextLine();
        System.out.print("Nationality: "); String nationality = sc.nextLine();
        System.out.print("District: "); String district = sc.nextLine();
        System.out.print("State: "); String state = sc.nextLine();

        String accNum = "AC" + new Random().nextInt(100000);
        String ifsc = "BANK0001234";

        PreparedStatement ps = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)");
        ps.setString(1, accNum); ps.setString(2, name); ps.setString(3, father);
        ps.setInt(4, age); ps.setString(5, phone); ps.setString(6, address);
        ps.setString(7, aadhar); ps.setString(8, gender); ps.setString(9, nationality);
        ps.setString(10, district); ps.setString(11, state); ps.setString(12, ifsc);

        ps.executeUpdate();
        System.out.println("Registered! Account Number: " + accNum + ", IFSC: " + ifsc);
    }
}