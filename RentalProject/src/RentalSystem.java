import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RentalSystem {
	private static RentalSystem rentalSystemInstance = null;
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    private static String VEHICLE_STORAGE_FILE = "./vehicles.txt";
    private static String CUSTOMER_STORAGE_FILE = "./customers.txt";
    private static String RENTAL_RECORDS_STORAGE_FILE = "./rental_records.txt";
    
    private RentalSystem()
    {
    }
    
    public static synchronized RentalSystem getInstance()
    {
    	if (rentalSystemInstance == null)
    		rentalSystemInstance = new RentalSystem();
    	
    	return rentalSystemInstance;
    }
    
    private void saveVehicle(Vehicle aVehicle)
    {
    	File out = new File(VEHICLE_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		fr.write(aVehicle.getLicensePlate() + " " +
    				aVehicle.getMake() + " " +
    				aVehicle.getModel() + " " +
    				Integer.toString(aVehicle.getYear()) + " " +
    				aVehicle.getStatus() +
    				(aVehicle.getClass().equals(Car.class) ? " " + Integer.toString(((Car)aVehicle).getNumSeats()) : "") +
    				(aVehicle.getClass().equals(Motorcycle.class) ? " " + Boolean.toString(((Motorcycle)aVehicle).hasSidecar()) : "") +
    				(aVehicle.getClass().equals(Truck.class) ? " " + Double.toString(((Truck)aVehicle).getCargoCapacity()) : "") + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Vehicle data to file failed!");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Vehicle data file stream failed!");
    		}
    	}
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        this.saveVehicle(vehicle);
    }
    
    private void saveCustomer(Customer aCustomer)
    {
    	File out = new File(CUSTOMER_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		fr.write(Integer.toString(aCustomer.getCustomerId()) + " " + aCustomer.getCustomerName() + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Customer data to file failed!");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Customer data file stream failed!");
    		}
    	}
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        this.saveCustomer(customer);
    }
    
    private void saveRecord(RentalRecord aRecord)
    {
    	File out = new File(RENTAL_RECORDS_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		fr.write(aRecord.getRecordType().toString() + " " +
    				aRecord.getVehicle().getLicensePlate() + " " +
    				aRecord.getCustomer().toString().replace("| ", "").replace("Customer ID: ", "").replace("Name: ", "") + " " +	/* Work around `toString''s output by removing extra stuff. */
    				aRecord.getRecordDate().toString() + " " +
    				Double.toString(aRecord.getTotalAmount()) + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Customer data to file failed!");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Customer data file stream failed!");
    		}
    	}
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            this.saveRecord(rentalHistory.getLast());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            this.saveRecord(rentalHistory.getLast());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }

    public void displayVehicles(boolean onlyAvailable) {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (!onlyAvailable || v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId() == Integer.parseInt(id))
                return c;
        return null;
    }
}