import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class VehicleRentalAppTest {

	/* Test license plate validation with four test vehicles.
	 * Each vehicle is tested for throwing an exception when a bad plate is given.
	 * Valid plates should be assigned without throwing an exception, and their contents are checked once more.
	 */
	@Test
	void testLicensePlateValidation()
	{
		Vehicle testVehicle1 = new Car("Make1", "Model1", 2022, 2);
		Vehicle testVehicle2 = new Car("Make2", "Model2", 2023, 3);
		Vehicle testVehicle3 = new Car("Make3", "Model3", 2024, 4);
		Vehicle testVehicle4 = new Car("Make4", "Model4", 2025, 5);
		
		/* Test for invalid license plates throwing an exception. */
		Exception exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
			testVehicle1.setLicensePlate(null);
		});
		Exception exceptionEmpty = assertThrows(IllegalArgumentException.class, () -> {
			testVehicle2.setLicensePlate(" ");
		});
		Exception exceptionTooManyNumbers = assertThrows(IllegalArgumentException.class, () -> {
			testVehicle3.setLicensePlate("AAA1000");
		});
		Exception exceptionTooFewNumbers = assertThrows(IllegalArgumentException.class, () -> {
			testVehicle4.setLicensePlate("ZZZ99");
		});
		
		assertTrue(exceptionNull.getMessage().equals("Plate is invalid!"));
		assertTrue(exceptionEmpty.getMessage().equals("Plate is invalid!"));
		assertTrue(exceptionTooManyNumbers.getMessage().equals("Plate is invalid!"));
		assertTrue(exceptionTooFewNumbers.getMessage().equals("Plate is invalid!"));
		
		/* Test that valid license plates do not throw an assertion. Plates should also be equal to the given tested input. */
		assertDoesNotThrow(() -> {
			testVehicle1.setLicensePlate("AAA100");
	    });
		assertTrue(testVehicle1.getLicensePlate().equals("AAA100"));
		
		assertDoesNotThrow(() -> {
			testVehicle2.setLicensePlate("ABC567");
	    });
		assertTrue(testVehicle2.getLicensePlate().equals("ABC567"));
		
		assertDoesNotThrow(() -> {
			testVehicle3.setLicensePlate("ZZZ999");
	    });
		assertTrue(testVehicle3.getLicensePlate().equals("ZZZ999"));
	}

	/* Test the renting and returning logic. Renting the vehicle twice should not work,
	 * and returning the vehicle twice should not work. The customer and vehicle objects
	 * are also tested before use for validity.
	 */
	@Test
	void testRentAndReturnVehicle()
	{
		/* Make a dummy car and assert that it's not null. */
		Vehicle testVehicle1 = new Car("Make1", "Model1", 2022, 2);
		assertTrue(testVehicle1 != null);
		
		/* Set its license plate. Make sure that it succeeds and is set to the right string. */
		assertDoesNotThrow(() -> {
			testVehicle1.setLicensePlate("AAA100");
	    });
		assertTrue(testVehicle1.getLicensePlate().equals("AAA100"));
		
		/* Make a dummy customer and ensure that it is set up correctly. */
		Customer testCustomer1 = new Customer(903, "Samantha");
		assertTrue(testCustomer1 != null);
		assertTrue(testCustomer1.getCustomerId() == 903);
		
		RentalSystem rentalSystem = RentalSystem.getInstance();
		
		/* Let the rental system pick up our test objects. */
		rentalSystem.addCustomer(testCustomer1);
		rentalSystem.addVehicle(testVehicle1);
		
		/* Test renting the vehicle. */
		assertTrue(rentalSystem.rentVehicle(testVehicle1, testCustomer1, LocalDate.now(), 0));
		assertTrue(testVehicle1.getStatus() == Vehicle.VehicleStatus.RENTED);
		
		/* Renting again should not work. */
		assertFalse(rentalSystem.rentVehicle(testVehicle1, testCustomer1, LocalDate.now(), 0));
		
		/* Returning the vehicle should succeed. */
		assertTrue(rentalSystem.returnVehicle(testVehicle1, testCustomer1, LocalDate.now(), 0));
		assertTrue(testVehicle1.getStatus() == Vehicle.VehicleStatus.AVAILABLE);
		
		/* Returning again should not work. */
		assertFalse(rentalSystem.returnVehicle(testVehicle1, testCustomer1, LocalDate.now(), 0));
	}
	
	/* Test the `RentalSystem' class for adherence to the singleton design pattern. */
	@Test
	void testSingletonRentalSystem()
	{
		try {
			/* Try getting the `RentalSystem''s class declared constructor, and verify that its only constructor is marked private. */
			Constructor<RentalSystem> rentalSystemConstructor = RentalSystem.class.getDeclaredConstructor();
			assertEquals(rentalSystemConstructor.getModifiers(), Modifier.PRIVATE);
			/* Make sure that getting the class's single instance succeeds. */
			assertTrue(RentalSystem.getInstance() != null);
	    } catch (ReflectiveOperationException e) {
	    	System.out.printf("Cannot get default constructor for the `RentalSystem' class!");
	    }
	}
}
