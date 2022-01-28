

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
public class SnackServiceImplTest {
	@Mock
    SnackRepository mockDao;

	// Tests for addNewSnack
	@Test
	public void addNewSnack_Must_Return_Added_Snack_When_Snack_Is_Valid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack toBeAdded2 = new Snack(0, "title", (float)1000.0);
		Snack saved2 = new Snack(2, "title", (float)1000.0);
		Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act
		Snack snack2 = service.addNewSnack(toBeAdded2);
		//Assert
		assertEquals(1, Mockito.mockingDetails(mockDao).getInvocations().size());
		assertSame(snack2, saved2);
	}
	
	@Test
	public void addNewSnack_Must_Throw_Illegal_Argument_Exeption_When_Price_Is_Non_Positive() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Snack toBeAdded = new Snack(0, "title", (float)-10.0);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewSnack(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Price can't be non-positive")) {
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
	public void addNewSnack_Must_Throw_Illegal_Argument_Exeption_When_Title_Is_Null_String() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Snack toBeAdded = new Snack(0, "", (float)10.0);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewSnack(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of snack is required")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	// tests for removeSnack
	@Test
	public void removeSnack_Must_Return_Removed_Snack_When_Id_Is_Valid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Snack saved = new Snack(5, "title", (float)10.0);
		List<BookingSnacks> bookings = new ArrayList<BookingSnacks>();
		saved.setBookings(bookings);
		Optional<Snack> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(5)).thenReturn(returned);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act
		Snack snack = service.removeSnack(5);
		
		//Assert
		Mockito.verify(mockDao).deleteById(5);;
		assertSame(snack, saved);
	}
	
	@Test
	public void removeSnack_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Optional<Snack> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeSnack(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Snack not found!!")) {
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
	public void findAll_Must_Return_List_Of_Snacks() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack snack1 = new Snack(5, "title", (float)10.0);
		Snack snack2 = new Snack(6, "title", (float)100.0);
		
		List<Snack> list = new ArrayList<Snack>();
		list.add(snack1);
		list.add(snack2);
		
		Mockito.when(mockDao.findAll()).thenReturn(list);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act
		List<Snack> returned = service.findAll();
		
		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
		assertTrue(returned.get(0) == snack1 && returned.get(1) == snack2);
	}
	
	// tests for findById
	@Test
	public void findById_Must_Return_Snack_When_Id_Is_Valid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Snack saved = new Snack(5, "title", (float)10.0);
		Optional<Snack> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(5)).thenReturn(returned);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act
		Snack snack = service.findById(5);
		
		//Assert
		Mockito.verify(mockDao).findById(5);;
		assertSame(snack, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		Optional<Snack> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Snack not found!!")) {
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
	public void updateById_Must_Return_Updated_Snack_When_Id_Is_Valid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack toBeCopied = new Snack(500, "new title", (float)10.0);
		Snack saved = new Snack(5, "title", (float)10.0);
		Snack toBeSaved = new  Snack(5, "new title", (float)10.0);
		
		Optional<Snack> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act
		Snack returned = service.updateById(toBeCopied, 5);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(Snack.class));
		System.out.println(returned);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void editSnackById_Must_Throw_Illegal_Argument_Exception_When_Price_Is_Non_Positive() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack toBeCopied = new Snack(500, "title", (float)-10.0);
		service.setFoodAndDrinksServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Price can't be non-positive")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editSnackById_Must_Throw_Illegal_Argument_Exception_When_Title_Is_Null() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack toBeCopied = new Snack(500, "", (float)10.0);
		service.setFoodAndDrinksServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of snack is required")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editSnackById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		SnackServiceImpl service = new SnackServiceImpl();
		
		Snack toBeCopied = new Snack(500, "new title", (float)10.0);
		Snack saved = new Snack(5, "title", (float)10.0);
		
		Optional<Snack> found = Optional.empty();
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		service.setFoodAndDrinksServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.updateById(toBeCopied, 5);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Snack not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao, Mockito.never()).save(any(Snack.class));
	}

}
