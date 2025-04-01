public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }
    
    public Vehicle(String make, String model, int year) {
    	if (make == null || make.isEmpty())
    		this.make = null;
    	else
    		this.make = capitalize(make);
    	
    	if (model == null || model.isEmpty())
    		this.model = null;
    	else
    		this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }
    
    private String capitalize(String aString)
    {
    	return aString != null ? aString.substring(0, 1).toUpperCase() + aString.substring(1).toLowerCase() : "";
    }
    
    /* Regex is wonderful. If the plate is not null, not empty, and the plate matches any three letters (insensitive of case) followed by any three numbers, then the plate is valid. */
    private boolean isValidPlate(String plate)
    {
    	return (plate != null && !plate.isEmpty() && plate.matches("[A-Za-z]{3}[0-9]{3}")) ? true : false;
    }

    /* Use the above method to validate the given plate (throwing an exception if invalid), before assigning it to the current vehicle. */
    public void setLicensePlate(String plate) {
    	if (!isValidPlate(plate))
    		throw new IllegalArgumentException("Plate is invalid!");
        this.licensePlate = plate == null ? null : plate.toUpperCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
