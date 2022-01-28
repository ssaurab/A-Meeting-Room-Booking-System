


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;









import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javafx.util.Pair;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {

	@Mock
    BookingRepository mockDao;
	
	@Mock
	EmailService mockEmailService;

// Tests for Add New Booking
	@Test
	public void addNewBooking_Must_Return_Added_Booking_When_Booking_Is_Valid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Room room = new Room();
		room.setStatus("active");
		room.setCapacity(100);
		Booking toBeAdded = new Booking(0, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved = new Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
//		Mockito.when(mockEmailService.sendEmail((MimeMessagePreparator) ArgumentMatchers.any(MimeMessagePreparator.class))).thenReturn(saved);

		service.setDao(mockDao);
		service.setEmailService(mockEmailService);
		
		//Act
		Booking booking = service.addNewBooking(toBeAdded);
		//Assert
		assertEquals(1, Mockito.mockingDetails(mockDao).getInvocations().size());
//			Mockito.verify(mockDao, Mockito.times(2));
		assertSame(booking, saved);
	}
	
	@Test
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Client_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), null, new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking client can't be empty")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Booking_Room_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), null, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking Room can't be null")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Booking_RoomLayout_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, null, new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking Room Layout can't be empty")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Attendeed_LT_1() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 0, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("At least 1 attendee required")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_PaymentMethod_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "confirmed", "wrong paymentmethod", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Payment Method")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Status_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "wrong status", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Status")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Room_Is_Inactive() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("inactive");
		Booking toBeAdded = new Booking(0, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Inactive room can't be booked")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Date_Is_In_The_Past() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("active");
		Booking toBeAdded = new Booking(0, "1947-08-15", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("You cannot modify or make a booking of a past date!")) {
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
	public void addNewBooking_Must_Throw_Illegal_Argument_Exeption_When_Attendees_GT_Room_Capacity() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("active");
		room.setCapacity(100);
		Booking toBeAdded = new Booking(0, "2021-09-01", 12, 14, 10000000, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewBooking(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Number of attendees can't be greater than room capacity")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

// Tests for Remove Booking
	@Test
	public void removeBooking_Must_Return_Removed_Booking_When_Id_Is_Valid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking saved = new Booking(1, "2020-09-10", 12, 14, 10, "wrong status", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Optional<Booking> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		Booking booking = service.removeBooking(1);
		
		//Assert
		Mockito.verify(mockDao).deleteById(1);;
		assertSame(booking, saved);
	}
	
	@Test
	public void removeBooking_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Optional<Booking> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeBooking(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);	
	}

// Tests for findAll
	@Test
	public void findAll_Must_Return_List_Of_Bookings() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		
		List<Booking> list = new ArrayList<Booking>();
		list.add(booking1);
		list.add(booking2);
		
		Mockito.when(mockDao.findAll()).thenReturn(list);
		service.setDao(mockDao);
		
		//Act
		List<Booking> returned = service.findAll();
		
		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
		assertTrue(returned.get(0) == booking1 && returned.get(1) == booking2);
	}

// Tests for findById
	@Test
	public void findById_Must_Return_Booking_When_Id_Is_Valid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking saved = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Optional<Booking> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		Booking booking = service.findById(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertSame(booking, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Optional<Booking> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
	
// Tests for editById
	@Test
	public void editBookingById_Must_Return_Edited_Booking_When_Id_Is_Valid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Room room = new Room();
		room.setStatus("active");
		room.setCapacity(100);
		Booking toBeCopied = new Booking(1000, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved = new Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking toBeSaved = new  Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Optional<Booking> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setDao(mockDao);
		
		//Act
		Booking returned = service.editBookingById(saved.getId(), toBeCopied);
		
		//Assert
//		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(Booking.class));
		System.out.println(returned);
		System.out.println(toBeSaved);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Client_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();

		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), null, new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking client can't be empty")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Room_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), null, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking Room can't be null")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_RoomLayout_Is_Null() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, null, new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking Room Layout can't be empty")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Attendeed_LT_1() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Booking toBeCopied = new Booking(1000,  "2020-09-10", 12, 14, 0, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("At least 1 attendee required")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_PaymentMethod_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "wrong paymentmethod", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Payment Method")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Status_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "wrong status", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), new Room(), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Status")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Room_Is_Inactive() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("inactive");
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Inactive room can't be booked")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Date_Is_In_The_Past() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("active");
		Booking toBeCopied = new Booking(1000, "1947-08-15", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("You cannot modify or make a booking of a past date!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Attendees_GT_Room_Capacity() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room = new Room();
		room.setStatus("active");
		room.setCapacity(100);
		Booking toBeCopied = new Booking(1000, "2021-08-15", 12, 14, 100000, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Number of attendees can't be greater than room capacity")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editBookingById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		Room room = new Room();
		room.setStatus("active");
		room.setCapacity(100);
		Booking toBeCopied = new Booking(1000, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), room, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		
		Optional<Booking> found = Optional.empty();
		Mockito.when(mockDao.findById(5)).thenReturn(found);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.editBookingById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Booking not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(5);
		Mockito.verify(mockDao, Mockito.never()).save(any(Booking.class));
	}
	
	
	@Test
	public void countBookedRoomByRoomId_Returns_Integer() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Mockito.when(mockDao.countByRoomId(5)).thenReturn((long) 3);
		service.setDao(mockDao);
		
		//Act and Assert
		Long received = service.countBookedRoomByRoomId(5);
		
		//Assert
		Mockito.verify(mockDao).countByRoomId(5);
		assertTrue(received ==(long)3);
	}
	
	
	@Test
	public void findSlotBookings_Returns_List_Of_Pairs_Of_FromHour_And_ToHour_Of_Matching_Date_And_RoomId() {
		//Arrange
		BookingServiceImpl service = new BookingServiceImpl();
		
		Room room1 = new Room(), room2 = new Room();
		room1.setId(5);
		room2.setId(10);
		Booking saved1 = new Booking(1, "2021-09-10", 2, 4, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved2 = new Booking(1, "2000-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved3 = new Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved4 = new Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved5 = new Booking(1, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		List<Booking> savedBookings = new ArrayList<Booking>();
		savedBookings.add(saved1);
		savedBookings.add(saved2);
		savedBookings.add(saved3);
		savedBookings.add(saved4);
		savedBookings.add(saved5);
		
		List<Pair<Integer, Integer>> toBeReturned = new ArrayList<Pair<Integer, Integer>>();
		toBeReturned.add(new Pair<Integer, Integer>(saved1.getFromHour(), saved1.getToHour()));
		toBeReturned.add(new Pair<Integer, Integer>(saved3.getFromHour(), saved3.getToHour()));
		
		Mockito.when(mockDao.findAll()).thenReturn(savedBookings);
		service.setDao(mockDao);
		
		//Act and Assert
		List<Pair<Integer, Integer>> returned = service.findSlotBookings("2021-09-10", 5);
		
		//Assert
		Mockito.verify(mockDao).findAll();
		Assert.assertEquals(returned, toBeReturned);
	}
	
	
}

