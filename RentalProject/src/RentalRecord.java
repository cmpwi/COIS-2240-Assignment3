import java.time.LocalDate;

public class RentalRecord {
    private Vehicle vehicle;
    private Customer customer;
    private LocalDate recordDate;
    private double totalAmount;
    private String recordType; // "RENT" or "RETURN"

    public RentalRecord(Vehicle vehicle, Customer customer, LocalDate recordDate, double totalAmount, String recordType) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.recordDate = recordDate;
        this.totalAmount = totalAmount;
        this.recordType = recordType;
    }

    public Customer getCustomer(){
    	return customer;
    }
    
    public Vehicle getVehicle(){
    	return vehicle;
    }
    
    /* Getters added to obtain necessary information for re-constructing a `RentalRecord' object from a text file. */
    public String getRecordType() {
    	return recordType;
    }
    
    public LocalDate getRecordDate() {
    	return recordDate;
    }
    
    public double getTotalAmount() {
    	return totalAmount;
    }
    
    /* The previous implementation of `toString()' was fine, but I felt like having two places of precision after the decimal point looked better. */
    @Override
    public String toString() {
        return String.format("%s | Plate: %s | Customer: %s | Date: %s | Amount: $%.2f", recordType, vehicle.getLicensePlate(), customer.getCustomerName(), recordDate, totalAmount);
    }
}