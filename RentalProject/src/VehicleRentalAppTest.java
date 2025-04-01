import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert.*;

import org.junit.jupiter.api.Test;

class VehicleRentalAppTest {

	@Test
	void testLicensePlateValidation() {
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
		
		/* Test that valid license plates do not throw an assertion. Plates should also not be not equal to the given tested input. */
		assertDoesNotThrow(() -> {
			testVehicle1.setLicensePlate("AAA100");
	    });
		
		assertFalse(!testVehicle1.getLicensePlate().equals("AAA100"));
		
		assertDoesNotThrow(() -> {
			testVehicle2.setLicensePlate("ABC567");
	    });
		
		assertFalse(!testVehicle2.getLicensePlate().equals("ABC567"));
		
		assertDoesNotThrow(() -> {
			testVehicle3.setLicensePlate("ZZZ999");
	    });
		
		assertFalse(!testVehicle3.getLicensePlate().equals("ZZZ999"));
	}

}
