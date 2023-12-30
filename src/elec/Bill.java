package elec;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
abstract class Payment{
	static void BillDetails() {
	}
}
public class Bill extends Payment{
	static Scanner sc=new Scanner(System.in);
    private int billNumber;
    private double unitsConsumed;
    private double amount;
    private String paymentStatus;
    private String month;

    public Bill(String month, double unitsConsumed, double amount) {
        this.billNumber = 0; // Set default bill number to 0
        this.unitsConsumed = unitsConsumed;
        this.amount = amount;
        this.paymentStatus = "Unpaid"; // Set default payment status to "Unpaid"
        this.month = month;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void Billdetails() {
        System.out.println("Bill Details:");
        System.out.println();
        System.out.println("Bill Number: " + getBillNumber());
        System.out.println("Month: " + getMonth());
        System.out.println("Units Consumed: " + getUnitsConsumed());
        System.out.println("Amount: " + getAmount());
        System.out.println("Payment Status: " + getPaymentStatus());
    }
    public void Billdetails(double amount,String payment_status) {
    	System.out.println();
    	 System.out.println("Amount: " + amount);
         System.out.println("Payment Status: " + payment_status);
    }

    public void generateBill(Connection connection) {
    	
        try {
        	
        	 System.out.print("Enter customer ID: ");
	            int customerId = sc.nextInt();

	            System.out.print("Enter month: ");
	            String month = sc.next();

	            System.out.print("Enter units consumed: ");
	            double unitsConsumed = sc.nextDouble();
             
	            
	            String pstat="Undone";
	            
	            double unitRate = 0.50; 
	            double amount = unitsConsumed * unitRate;
            String insertBillSQL = "INSERT INTO bills (customer_id, month, units_consumed, amount, payment_status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertBillStmt = connection.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS);
            insertBillStmt.setInt(1, customerId);
            insertBillStmt.setString(2, month);
            insertBillStmt.setDouble(3, unitsConsumed);
            insertBillStmt.setDouble(4, amount);
            insertBillStmt.setString(5, pstat);

            int affectedRows = insertBillStmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = insertBillStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int billNumber = generatedKeys.getInt(1);
                    setBillNumber(billNumber); 
                    System.out.println("Bill generated successfully! Bill ID: " + billNumber);
                    System.out.println();
                    String selectBillSQL = "SELECT * FROM bills WHERE customer_id = ?";
                    PreparedStatement selectBillStmt = connection.prepareStatement(selectBillSQL);
                    selectBillStmt.setInt(1, customerId);
                    ResultSet resultSet = selectBillStmt.executeQuery();

                    while (resultSet.next()) {
                        Bill bill = new Bill(
                                resultSet.getString("month"),
                                resultSet.getDouble("units_consumed"),
                                resultSet.getDouble("amount")
                        );
                        bill.setBillNumber(resultSet.getInt("bill_number"));
                        bill.setPaymentStatus(resultSet.getString("payment_status"));

                        bill.Billdetails();
                        System.out.println("-----------------------------------");
                    }
                }
            } else {
                System.out.println("Failed to generate bill.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    Bill(){
	
    }
    public static void viewBillDetails(Connection connection) {
        try {
        	System.out.print("Enter customer ID: ");
            int customerId = sc.nextInt();

            String selectBillSQL = "SELECT * FROM bills WHERE customer_id = ?";
            PreparedStatement selectBillStmt = connection.prepareStatement(selectBillSQL);
            selectBillStmt.setInt(1, customerId);
            ResultSet resultSet = selectBillStmt.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getString("month"),
                        resultSet.getDouble("units_consumed"),
                        resultSet.getDouble("amount")
                );
                bill.setBillNumber(resultSet.getInt("bill_number"));
                bill.setPaymentStatus(resultSet.getString("payment_status"));
                System.out.println();
                bill.Billdetails();
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePaymentStatus(Connection connection) {
        try {
        	 System.out.print("Enter bill number: ");
	            int billNumber = sc.nextInt();

	            System.out.print("Enter new payment status (Done/Undone): ");
	            String newPaymentStatus = sc.next();

	            String updatePaymentStatusSQL = "UPDATE bills SET payment_status = ? WHERE bill_number = ?";
	            PreparedStatement updatePaymentStatusStmt = connection.prepareStatement(updatePaymentStatusSQL);
	            updatePaymentStatusStmt.setString(1, newPaymentStatus);
	            updatePaymentStatusStmt.setInt(2, billNumber);
	            int affectedRows = updatePaymentStatusStmt.executeUpdate();

	            if (affectedRows > 0) {
	                System.out.println("Payment status updated successfully!");
	                String selectBillSQL = "SELECT * FROM bills WHERE customer_id = ?";
	                PreparedStatement selectBillStmt = connection.prepareStatement(selectBillSQL);
	                selectBillStmt.setInt(1, billNumber);
	                ResultSet resultSet = selectBillStmt.executeQuery();
                   
	                while (resultSet.next()) {
	                    String m=resultSet.getString("month");
	                    double c=resultSet.getDouble("units_consumed");
	                    double a=resultSet.getDouble("amount");
	                    Bill bill = new Bill(
	                            resultSet.getString("month"),
	                            resultSet.getDouble("units_consumed"),
	                            resultSet.getDouble("amount")
	                    );
	                    bill.setBillNumber(resultSet.getInt("bill_number"));
	                    bill.setPaymentStatus(resultSet.getString("payment_status"));
	                    String p=resultSet.getString("payment_status");
	                    bill.Billdetails(a,p);
	                } 
	            } else {
	                System.out.println("Failed to update payment status.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }
}
