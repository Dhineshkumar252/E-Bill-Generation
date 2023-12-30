package elec;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {
	static Scanner sc=new Scanner(System.in);
	private int id;
	Customer(){
		
	}
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCust_mail() {
		return cust_mail;
	}

	public void setCust_mail(String cust_mail) {
		this.cust_mail = cust_mail;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	private String name;
    private String phone;
    private String address;
    private String cust_mail;
    private String aadharNumber;
    public Customer(String name, String phone, String address, String cust_mail, String aadharNumber) {
        this.name=name;
        this.phone=phone;
        this.address=address;
        this.cust_mail=cust_mail;
        this.aadharNumber=aadharNumber;
    }

    public void displayDetailss() {
        System.out.println("Customer Name: "+ getName());
        System.out.println("Customer ID: " + getId());
        System.out.println("Phone: " + getPhone());
        System.out.println("Address: " + getAddress());
        System.out.println("Mail: " + getCust_mail());
        System.out.println("Adhar: " + getAadharNumber());
    }


    public void addNewCustomer(Connection connection) {
        try {
        	System.out.print("Enter name: ");
            String name = sc.next();

            System.out.print("Enter contact no: ");
            String phone = sc.next();;

            System.out.print("Enter address: ");
            String address = sc.next();
            
            System.out.print("Enter mail: ");
            String cust_mail = sc.next();

            System.out.print("Enter Aadhar: ");
            String aadharNumber = sc.next();
            
            String insertCustomerSQL = "INSERT INTO customers (name, phone, address, cust_mail, aadhar_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertCustomerStmt = connection.prepareStatement(insertCustomerSQL, Statement.RETURN_GENERATED_KEYS);
            insertCustomerStmt.setString(1, name);
            insertCustomerStmt.setString(2, phone);
            insertCustomerStmt.setString(3, address);
            insertCustomerStmt.setString(4, cust_mail);
            insertCustomerStmt.setString(5, aadharNumber);

            int affectedRows = insertCustomerStmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = insertCustomerStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    setId(customerId); // Set the generated customer ID
                    System.out.println();
                    System.out.println("Customer added successfully! Customer ID: " + customerId);
                    System.out.println();
                    String selectCustomerSQL = "SELECT * FROM customers WHERE id = ?";
    	            PreparedStatement selectCustomerStmt = connection.prepareStatement(selectCustomerSQL);
    	            selectCustomerStmt.setInt(1, customerId);
    	            ResultSet resultSet = selectCustomerStmt.executeQuery();

    	            if (resultSet.next()) {
    	                Customer customer = new Customer(
    	                        resultSet.getString("name"),
    	                        resultSet.getString("phone"),
    	                        resultSet.getString("address"),
    	                        resultSet.getString("cust_mail"),
    	                        resultSet.getString("aadhar_number")
    	                );
    	                customer.setId(customerId);
    	                customer.setId(resultSet.getInt("id"));
    	                System.out.println("Customer Details:");
    	                customer.displayDetailss();
    	            } else {
    	                System.out.println("Customer not found.");
    	            }
                }
            } else {
                System.out.println("Failed to add new customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewCustomerDetails(Connection connection) {
        try {
        	    System.out.print("Enter customer ID: ");
	            int customerId = sc.nextInt();

	            String selectCustomerSQL = "SELECT * FROM customers WHERE id = ?";
	            PreparedStatement selectCustomerStmt = connection.prepareStatement(selectCustomerSQL);
	            selectCustomerStmt.setInt(1, customerId);
	            ResultSet resultSet = selectCustomerStmt.executeQuery();

	            if (resultSet.next()) {
	                Customer customer = new Customer(
	                        resultSet.getString("name"),
	                        resultSet.getString("phone"),
	                        resultSet.getString("address"),
	                        resultSet.getString("cust_mail"),
	                        resultSet.getString("aadhar_number")
	                );
	                customer.setId(customerId);
	                customer.setId(resultSet.getInt("id"));
	                System.out.println();
	                System.out.println("Customer Details:");
	                customer.displayDetailss();
	            } else {
	                System.out.println("Customer not found.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
    
}
