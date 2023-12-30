package elec;


import java.sql.*;
import java.util.Scanner;
public class Electric {


	    private static Connection connection;
	    private static Statement statement;
	    static Scanner sc = new Scanner(System.in);

	    public static void main(String[] args) {
	        try {

	            String username = "root";
	            String password = "Dhinesh007#";
	            String url = "jdbc:mysql://localhost:3306/electric_billing_system";
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(url, username, password);
	            statement = connection.createStatement();

	            createTables();

	            showMenu();

	            connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static void createTables() throws SQLException {
	        String createCustomerTableSQL = "CREATE TABLE IF NOT EXISTS customers (" +
	                "id INT AUTO_INCREMENT PRIMARY KEY," +
	                "name VARCHAR(255) NOT NULL," +
	                "phone VARCHAR(20) NOT NULL," +
	                "address VARCHAR(255) NOT NULL," +
	                "cust_mail VARCHAR(255) NOT NULL," +
	                "aadhar_number VARCHAR(20) NOT NULL)";
	        statement.executeUpdate(createCustomerTableSQL);

	        String createBillTableSQL = "CREATE TABLE IF NOT EXISTS bills (" +
	                "bill_number INT AUTO_INCREMENT PRIMARY KEY," +
	                "customer_id INT NOT NULL," +
	                "month VARCHAR(10) NOT NULL," +
	                "units_consumed DOUBLE NOT NULL," +
	                "amount DOUBLE NOT NULL," +
	                "payment_status VARCHAR(10) NOT NULL)";
	        statement.executeUpdate(createBillTableSQL);
	    }
	    

	    private static void showMenu() {
	        while (true) {
	            System.out.println("\n1. Add new customer");
	            System.out.println("2. Generate the bill");
	            System.out.println("3. Update payment status");
	            System.out.println("4. View bill Description");
	            System.out.println("5. View customer details");
	            System.out.println("6. Exit");
	            System.out.print("Enter your choice: ");

	            int choice = sc.nextInt();
	            Customer customer=new Customer();
	            Bill bill=new Bill();
	            switch (choice) {
	                case 1:
	                    customer.addNewCustomer(connection);
	                    break;
	                case 2:
	                    bill.generateBill(connection);
	                    break;
	                case 3:
	                	bill.updatePaymentStatus(connection);
	                    break;
	                case 4:
	                    bill.viewBillDetails(connection);
	                    break;
	                case 5:
	                	customer.viewCustomerDetails(connection);
	                    break;
	                case 6:
	                    System.out.println("Terminating...");
	                    return;
	                default:
	                    System.out.println("Invalid choice. Please try again.");
	            }
	        }
	    }

	}


