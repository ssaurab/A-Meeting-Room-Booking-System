

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;





@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceImplTest {
	@Mock
	EquipmentRepository mockDao;


	// Tests for addNewEquipment
	@Test
	public void addNewEquipment_Must_Return_Added_Equipment_When_Equipment_Is_Valid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeAdded = new Equipment(0, "title", true, "perHour", (float)1000.0);
		Equipment saved = new Equipment(1, "title", true, "perHour", (float)1000.0);
		Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
		
		Equipment toBeAdded2 = new Equipment(0, "title", true, "perBooking", (float)1000.0);
		Equipment saved2 = new Equipment(2, "title", true, "perBooking", (float)1000.0);
		Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act
		Equipment equipment = service.addNewEquipment(toBeAdded);
		Equipment equipment2 = service.addNewEquipment(toBeAdded2);
		//Assert
		assertEquals(2, Mockito.mockingDetails(mockDao).getInvocations().size());
//		Mockito.verify(mockDao, Mockito.times(2));
		assertSame(equipment, saved);
		assertSame(equipment2, saved2);
	}
	
	@Test
	public void addNewEquipment_Must_Throw_Illegal_Argument_Exeption_When_Price_Is_Non_Positive() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Equipment toBeAdded = new Equipment(0, "title", true, "perHour", (float)-10.0);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewEquipment(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("price can't be 0")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewEquipment_Must_Throw_Illegal_Argument_Exeption_When_Title_Is_Null_String() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Equipment toBeAdded = new Equipment(0, "", true, "perHour", (float)10.0);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewEquipment(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of Equipment is missing!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewEquipment_Must_Throw_Illegal_Argument_Exeption_When_PriceType_Is_Invalid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Equipment toBeAdded = new Equipment(0, "title", true, "wrong pricetype", (float)10.0);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewEquipment(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Parameter to PriceType")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	// tests for removeEquipment
	@Test
	public void removeEquipment_Must_Return_Removed_Equipment_When_Id_Is_Valid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Equipment saved = new Equipment(5, "title", true, "perHour", (float)10.0);
		List<BookingEquipments> bookings = new ArrayList<BookingEquipments>();
		saved.setBookings(bookings);
		Optional<Equipment> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(5)).thenReturn(returned);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act
		Equipment equipment = service.removeEquipment(5);
		
		//Assert
		Mockito.verify(mockDao).deleteById(5);;
		assertSame(equipment, saved);
	}
	
	@Test
	public void removeEquipment_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Optional<Equipment> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeEquipment(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Equipment not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);	
	}
	
	// tests for findAll
	@Test
	public void findAll_Must_Return_List_Of_Equipments() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment equipment1 = new Equipment(5, "title", true, "perHour", (float)10.0);
		Equipment equipment2 = new Equipment(6, "title", true, "perHour", (float)100.0);
		
		List<Equipment> list = new ArrayList<Equipment>();
		list.add(equipment1);
		list.add(equipment2);
		
		Mockito.when(mockDao.findAll()).thenReturn(list);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act
		List<Equipment> returned = service.findAll();
		
		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
		assertTrue(returned.get(0) == equipment1 && returned.get(1) == equipment2);
	}
	
	// tests for findById
	@Test
	public void findById_Must_Return_Equipment_When_Id_Is_Valid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Equipment saved = new Equipment(5, "title", true, "perHour", (float)10.0);
		Optional<Equipment> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(5)).thenReturn(returned);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act
		Equipment equipment = service.findById(5);
		
		//Assert
		Mockito.verify(mockDao).findById(5);;
		assertSame(equipment, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		Optional<Equipment> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Equipment not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
	
	// tests for updateById
	@Test
	public void editById_Must_Return_Updated_Equipment_When_Id_Is_Valid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeCopied = new Equipment(500, "new title", true, "perHour", (float)10.0);
		Equipment saved = new Equipment(5, "title", true, "perHour", (float)10.0);
		Equipment toBeSaved = new  Equipment(5, "new title", true, "perHour", (float)10.0);
		
		Optional<Equipment> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act
		Equipment returned = service.updateById(toBeCopied, 5);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(Equipment.class));
		System.out.println(returned);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void editEquipmentById_Must_Throw_Illegal_Argument_Exception_When_Price_Is_Non_Positive() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeCopied = new Equipment(500, "title", true, "perHour", (float)-10.0);
		service.setEquipmentsServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("price can't be 0")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editEquipmentById_Must_Throw_Illegal_Argument_Exception_When_Title_Is_Null() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeCopied = new Equipment(500, "", true, "perHour", (float)10.0);
		service.setEquipmentsServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of Equipment is missing!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editEquipmentById_Must_Throw_Illegal_Argument_Exception_When_PriceType_Is_Invalid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeCopied = new Equipment(500, "title", true, "wrong pricetype", (float)10.0);
		service.setEquipmentsServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Parameter to PriceType")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editEquipmentById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		EquipmentServiceImpl service = new EquipmentServiceImpl();
		
		Equipment toBeCopied = new Equipment(500, "new title", true, "perHour", (float)10.0);
		Equipment saved = new Equipment(5, "title", true, "perHour", (float)10.0);
		
		Optional<Equipment> found = Optional.empty();
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		service.setEquipmentsServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Equipment not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao, Mockito.never()).save(any(Equipment.class));
	}
}
