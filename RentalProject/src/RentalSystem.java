import java.util.List;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
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
    	/* Load all data from files on disk when the `RentalSystem' starts up. */
    	loadData();
    }
    
    /* Singleton getter for the `RentalSystem' class. */
    public static synchronized RentalSystem getInstance()
    {
    	if (rentalSystemInstance == null)
    		rentalSystemInstance = new RentalSystem();
    	
    	return rentalSystemInstance;
    }
    
    /* Called by `RentalSystem' to load vehicle, customer, and rental record data from text files. */
    private void loadData()
    {
    	/* Load Vehicle data into `vehicles' array. */
    	BufferedReader vehicleBufferedReader = null;
    	try {
    		vehicleBufferedReader = new BufferedReader(new FileReader(VEHICLE_STORAGE_FILE));
    		String vehicleLine = vehicleBufferedReader.readLine();
    		
    		/* Until we reach the end of the file... */
    		while (vehicleLine != null) {
    			/* Split the current line over whitespace boundaries. */
    			String[] vehicleSplitLine = vehicleLine.split("\\s+");
    			
    			if (vehicleSplitLine[0].contentEquals("Car")) {				/* If we're a Car, make a car object. */
    				Car newCar = new Car(vehicleSplitLine[1], vehicleSplitLine[2], Integer.parseInt(vehicleSplitLine[3]), Integer.parseInt(vehicleSplitLine[4]));
    				newCar.setLicensePlate(vehicleSplitLine[5]);
    				vehicles.add(newCar);
    			}
    			else if (vehicleSplitLine[0].contentEquals("Motorcycle")) {	/* If we're a Motorcycle, make a motorcycle object. */
    				Motorcycle newMotorcycle = new Motorcycle(vehicleSplitLine[1], vehicleSplitLine[2], Integer.parseInt(vehicleSplitLine[3]), Boolean.parseBoolean(vehicleSplitLine[4]));
    				newMotorcycle.setLicensePlate(vehicleSplitLine[5]);
    				vehicles.add(newMotorcycle);
    			}
    			else if (vehicleSplitLine[0].contentEquals("Truck")) {		/* If we're a Truck, make a truck object. */
    				Truck newTruck = new Truck(vehicleSplitLine[1], vehicleSplitLine[2], Integer.parseInt(vehicleSplitLine[3]), Double.parseDouble(vehicleSplitLine[4]));
    				newTruck.setLicensePlate(vehicleSplitLine[5]);
    				vehicles.add(newTruck);
    			}
    			
    			/* Advance to the next line in the file. */
    			vehicleLine = vehicleBufferedReader.readLine();
    		}
    		/* Close the file when we're all done. */
    		vehicleBufferedReader.close();
    	} catch (IOException e) {
    		System.out.printf("Reading Vehicle data from file failed!\n");
    	}
    	
    	/* Load Customer data into `customers' array. */
    	BufferedReader customerBufferedReader = null;
    	try {
    		customerBufferedReader = new BufferedReader(new FileReader(CUSTOMER_STORAGE_FILE));
    		String customerLine = customerBufferedReader.readLine();
    		
    		String fullCustomerName = "";

    		while (customerLine != null) {
    			/* Split the current line over whitespace boundaries. */
    			String[] customerSplitLine = customerLine.split("\\s+");
    			/* Construct the name from the customer record. */
    			for (int i = 1; i < customerSplitLine.length; ++i) {
    				if (i > 1)	/* If we have more than one word in the whole name, add a space in between. */
    					fullCustomerName = fullCustomerName.concat(" ");
    				fullCustomerName = fullCustomerName.concat(customerSplitLine[i]);
    			}
    			customers.add(new Customer(Integer.parseInt(customerSplitLine[0]), fullCustomerName));
    			System.out.printf("DEBUG: %d %s\n", customers.get(customers.size() - 1).getCustomerId(), customers.get(customers.size() - 1).getCustomerName());
    			
    			/* Reset the full customer name for the next iteration. */
    			fullCustomerName = "";
    			
    			/* Advance to the next line in the file. */
    			customerLine = customerBufferedReader.readLine();
    		}
    		/* Close the file when we're all done. */
    		customerBufferedReader.close();
    	} catch (IOException e) {
    		System.out.printf("Reading Customer data from file failed!\n");
    	}
    	
    	/* Load RentalHistory data into `rentalHistory' array. */
    	BufferedReader rhBufferedReader = null;
    	try {
    		rhBufferedReader = new BufferedReader(new FileReader(RENTAL_RECORDS_STORAGE_FILE));
    		String rhLine = rhBufferedReader.readLine();
    		
    		while (rhLine != null) {
    			/* Split the current line over whitespace boundaries. */
    			String[] rhSplitLine = rhLine.split("\\s+");
    			
    			Vehicle addedVehicle = null;
    			/* Get and compare all the license plates to match against a vehicle in our array. */
    			for (int i = 0; i < vehicles.size(); ++i) {
    				if (vehicles.get(i).getLicensePlate().contentEquals(rhSplitLine[0])) {
    					addedVehicle = vehicles.get(i);
    					break;
    				}
    			}
    			
    			Customer addedCustomer = null;
    			String checkedCustomerName = "";

    			/* Iterate through the entirety of the customer objects. We compare their customer IDs and names to what we find in the rental records. */
    			for (int i = 0; i < customers.size(); ++i) {

    				/* Construct the name from the rental record. */
    				for (int j = 2; j < (rhSplitLine.length - 3); ++j) {
    					if (j > 2)	/* If we have more than one word in the whole name, add a space in between. */
    						checkedCustomerName = checkedCustomerName.concat(" ");
    					checkedCustomerName = checkedCustomerName.concat(rhSplitLine[j]);
    				}
    				
    				/* If the customer's ID and the record's ID match, and the customer's name and record name match, then this is our customer. */
    				if ((Integer.compare(customers.get(i).getCustomerId(), Integer.parseInt(rhSplitLine[1])) == 0) && (customers.get(i).getCustomerName().contentEquals(checkedCustomerName))) {
    					addedCustomer = customers.get(i);
    					break;
    				}
    				
    				/* Reset the checked customer name for the next iteration. */
    				checkedCustomerName = "";
    			}

    			/* Once we found our customer, make the new rental record and add it to the rental history. */
    			rentalHistory.addRecord(new RentalRecord(addedVehicle, addedCustomer, LocalDate.parse(rhSplitLine[rhSplitLine.length - 3]), Double.parseDouble(rhSplitLine[rhSplitLine.length - 2]), rhSplitLine[rhSplitLine.length - 1]));
    			
    			/*
    			 * Since we now have (and assume) that our customer exists and is not null, we check the vehicle status from the rental records.
    			 * All vehicles are initialized from their file as being available, even though this is not universally true in all execution states.
    			 * This happens because the vehicles list is only updated when new vehicles are added, hence requiring a full traversal of the rental
    			 * records to ensure a valid vehicle availability status.
    			 */
    			if (rhSplitLine[rhSplitLine.length - 1].equals("RENT") && (addedVehicle.getStatus().compareTo(Vehicle.VehicleStatus.AVAILABLE) == 0)) {
    				addedVehicle.setStatus(Vehicle.VehicleStatus.RENTED);
    				System.out.printf("DEBUG: Vehicle %s status set to RENTED.\n", addedVehicle.getLicensePlate());
    			} else if (rhSplitLine[rhSplitLine.length - 1].equals("RETURN") && (addedVehicle.getStatus().compareTo(Vehicle.VehicleStatus.RENTED) == 0)) {
    				addedVehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
    				System.out.printf("DEBUG: Vehicle %s status set to AVAILABLE.\n", addedVehicle.getLicensePlate());
    			}
    			
    			/* Advance to the next line in the file. */
    			rhLine = rhBufferedReader.readLine();
    		}
    		/* Close the file when we're all done. */
    		rhBufferedReader.close();
    	} catch (IOException e) {
    		System.out.printf("Reading RentalHistory data from file failed!\n");
    	}
    }
    
    private void saveVehicle(Vehicle aVehicle)
    {
    	File out = new File(VEHICLE_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		/* Open the file in append mode, and write a vehicle object's information out to `./vehicles.txt'. */
    		fr.write((aVehicle.getClass().equals(Car.class) ? "Car" : "") +
    				(aVehicle.getClass().equals(Motorcycle.class) ? "Motorcycle" : "") +
    				(aVehicle.getClass().equals(Truck.class) ? "Truck" : "") +
    				" " + aVehicle.getMake() + " " +
    				aVehicle.getModel() + " " +
    				Integer.toString(aVehicle.getYear()) +
    				(aVehicle.getClass().equals(Car.class) ? " " + Integer.toString(((Car)aVehicle).getNumSeats()) : "") +
    				(aVehicle.getClass().equals(Motorcycle.class) ? " " + Boolean.toString(((Motorcycle)aVehicle).hasSidecar()) : "") +
    				(aVehicle.getClass().equals(Truck.class) ? " " + Double.toString(((Truck)aVehicle).getCargoCapacity()) : "") +
    				" " + aVehicle.getLicensePlate() + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Vehicle data to file failed!\n");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Vehicle data file stream failed!\n");
    		}
    	}
    }

    public boolean addVehicle(Vehicle vehicle) {
    	if (findVehicleByPlate(vehicle.getLicensePlate()) != null)
    		return false;

        vehicles.add(vehicle);
        this.saveVehicle(vehicle);
        return true;
    }
    
    private void saveCustomer(Customer aCustomer)
    {
    	File out = new File(CUSTOMER_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		/* Open the file in append mode, and write a customer object's information out to `./customers.txt'. */
    		fr.write(Integer.toString(aCustomer.getCustomerId()) + " " + aCustomer.getCustomerName() + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Customer data to file failed!\n");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Customer data file stream failed!\n");
    		}
    	}
    }

    public boolean addCustomer(Customer customer) {
    	if (findCustomerById(Integer.toString(customer.getCustomerId())) != null)
    		return false;

        customers.add(customer);
        this.saveCustomer(customer);
        return true;
    }
    
    private void saveRecord(RentalRecord aRecord)
    {
    	File out = new File(RENTAL_RECORDS_STORAGE_FILE);
    	FileWriter fr = null;
    	try {
    		fr = new FileWriter(out, true);
    		/* Open the file in append mode, and write a rental record object's information out to `./rental_records.txt'. */
    		fr.write(aRecord.getVehicle().getLicensePlate() + " " +
    				aRecord.getCustomer().toString().replace("| ", "").replace("Customer ID: ", "").replace("Name: ", "") + " " +	/* Work around `toString''s output by removing extra stuff. */
    				aRecord.getRecordDate().toString() + " " +
    				Double.toString(aRecord.getTotalAmount()) + " " + 
    				aRecord.getRecordType().toString() + "\n");
    	} catch (IOException e) {
    		System.out.printf("Writing Customer data to file failed!\n");
    	} finally {
    		try {
    			fr.close();
    		} catch (IOException e) {
    			System.out.printf("Closing Customer data file stream failed!\n");
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