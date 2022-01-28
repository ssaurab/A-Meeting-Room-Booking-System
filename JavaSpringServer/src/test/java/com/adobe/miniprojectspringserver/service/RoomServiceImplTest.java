









import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceImplTest
{
    @Mock
    RoomRepository mockDao;


    // Tests for Add New Room
    @Test
    public void addNewRoom_Must_Return_Added_Room_When_Room_Is_Valid() {
        //Arrange
        Room toBeAdded = new Room(0,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
        Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
        Room toBeAdded2 = new Room(0,"Panorama2",20,"active",null, "description", 10.0f, 1.0f);
        Room saved2 = new Room(2,"Panorama2",20,"active",null, "description", 10.0f, 1.0f);

        RoomServiceImpl service = new RoomServiceImpl();
        service.setRoomServiceDao(mockDao);
        Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
        Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
        //Act
        Room room = service.addNewRoom(toBeAdded);
        Room room2 = service.addNewRoom(toBeAdded2);
        //Assert
        assertEquals(2, Mockito.mockingDetails(mockDao).getInvocations().size());
//		Mockito.verify(mockDao, Mockito.times(2));
        assertSame(room, saved);
        assertSame(room2, saved2);
    }
    
    @Test
	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Name_Is_Null() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Room toBeAdded = new Room(0,"",20,"active",null, "description", 10.0f, 1.0f);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewRoom(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of room can't be empty")) {
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
	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Status_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Room toBeAdded = new Room(0,"name",20,"wrong status",null, "description", 10.0f, 1.0f);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewRoom(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Argument for status")) {
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
   	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Capacity_LT_1() {
   		//Arrange
   		RoomServiceImpl service = new RoomServiceImpl();
   		Room toBeAdded = new Room(0,"name",0,"active",null, "description", 10.0f, 1.0f);
   		service.setRoomServiceDao(mockDao);
   		
   		//Act and Assert
   	    try {
   	    	service.addNewRoom(toBeAdded);
   	    	fail(); // Should not reach here
   	    }
   	    catch (IllegalArgumentException e) {
   	    	if(!e.getMessage().equals("Capacity can't be less than 1")) {
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
   	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Price_Per_Day_LT_0() {
   		//Arrange
   		RoomServiceImpl service = new RoomServiceImpl();
   		Room toBeAdded = new Room(0,"name",20,"active",null, "description", -100f, 1.0f);
   		service.setRoomServiceDao(mockDao);
   		
   		//Act and Assert
   	    try {
   	    	service.addNewRoom(toBeAdded);
   	    	fail(); // Should not reach here
   	    }
   	    catch (IllegalArgumentException e) {
   	    	if(!e.getMessage().equals("Invalid value for Price Per Day.")) {
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
   	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Price_Per_Hour_LT_0() {
   		//Arrange
   		RoomServiceImpl service = new RoomServiceImpl();
   		Room toBeAdded = new Room(0,"name",20,"active",null, "description", 10f, -100f);
   		service.setRoomServiceDao(mockDao);
   		
   		//Act and Assert
   	    try {
   	    	service.addNewRoom(toBeAdded);
   	    	fail(); // Should not reach here
   	    }
   	    catch (IllegalArgumentException e) {
   	    	if(!e.getMessage().equals("Invalid value for Price Per Hour.")) {
   	    		fail();
   	    	}
   	    }
   	    catch (Throwable e) {  // Not expected exception class
   	    	fail();
   	    }

   		//Assert
   		Mockito.verifyNoInteractions(mockDao);
   	}

 // Tests for Remove Room
 	@Test
 	public void removeRoom_Must_Return_Removed_Room_When_Id_Is_Valid() {
 		//Arrange
 		RoomServiceImpl service = new RoomServiceImpl();
 		Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
 		Set<Booking> bookings = new HashSet<Booking>();
 		saved.setBookings(bookings);
 		Optional<Room> returned = Optional.of(saved);
 		Mockito.when(mockDao.findById(1)).thenReturn(returned);
 		service.setRoomServiceDao(mockDao);
 		
 		//Act
 		Room room = service.removeRoom(1);
 		
 		//Assert
 		Mockito.verify(mockDao).deleteById(1);;
 		assertSame(room, saved);
 	}
 	
 	@Test
	public void removeRoom_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Optional<Room> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeRoom(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Room not found!!")) {
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
 	public void findAll_Must_Return_List_Of_Rooms() {
 		//Arrange
 		RoomServiceImpl service = new RoomServiceImpl();
 		
 		Room room1 = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
 		Room room2 = new Room(2,"Panorama 2",200,"active",null, "description", 10.0f, 1.0f);
 		
 		List<Room> list = new ArrayList<Room>();
 		list.add(room1);
 		list.add(room2);
 		
 		Mockito.when(mockDao.findAll()).thenReturn(list);
 		service.setRoomServiceDao(mockDao);
 		
 		//Act
 		List<Room> returned = service.findAll();
 		
 		//Assert
 		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
 		assertTrue(returned.get(0) == room1 && returned.get(1) == room2);
 	}
 	
 	// Tests for findById
	@Test
	public void findById_Must_Return_Room_When_Id_Is_Valid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
		Optional<Room> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act
		Room room = service.findById(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertSame(room, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Optional<Room> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Room not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
	
	// Tests for getAllRoomLayoutsAvailableForThisRoom
	@Test
	public void getAllRoomLayoutsAvailableForThisRoom_Must_Return_RoomLayouts_When_Id_Is_Valid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
		Set<RoomLayout> savedLayouts = new HashSet<RoomLayout>();
		savedLayouts.add(new RoomLayout(2,"Circle",null));
		savedLayouts.add(new RoomLayout(4,"Conference",null));
		savedLayouts.add(new RoomLayout(6,"A-shaped",null));
		saved.setLayouts(savedLayouts);
		Optional<Room> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act
		Set<RoomLayout> roomLayouts = service.getAllRoomLayoutsAvailableForThisRoom(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertTrue(roomLayouts.equals(savedLayouts));
	}
	
	@Test
	public void getAllRoomLayoutsAvailableForThisRoom_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Optional<Room> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.getAllRoomLayoutsAvailableForThisRoom(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Room not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
	
	// Tests for getAllBookingOfThisRoom
	@Test
	public void getAllBookingOfThisRoom_Must_Return_Bookings_When_Id_Is_Valid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
		Set<Booking> savedBookings = new HashSet<Booking>();
		savedBookings.add(new Booking(0, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), saved, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		savedBookings.add(new Booking(0, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), saved, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		savedBookings.add(new Booking(0, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(), saved, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		saved.setBookings(savedBookings);
		Optional<Room> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act
		Set<Booking> bookings = service.getAllBookingOfThisRoom(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertTrue(bookings.equals(savedBookings));
	}
	
	@Test
	public void getAllBookingOfThisRoom_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		Optional<Room> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.getAllBookingOfThisRoom(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Room not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
	
	// Tests for updateById
	@Test
	public void updateRoomById_Must_Return_Edited_Room_When_Id_Is_Valid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(100,"New Panorama",20,"active",null, "description", 10.0f, 1.0f);
		Room saved = new Room(1,"Panorama",20,"active",null, "description", 10.0f, 1.0f);
		Room toBeSaved = new  Room(1,"New Panorama",20,"active",null, "description", 10.0f, 1.0f);
		
		Optional<Room> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setRoomServiceDao(mockDao);
		
		//Act
		Room returned = service.updateRoomById(saved.getId(), toBeCopied);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(Room.class));
		System.out.println(returned);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Name_Is_Null() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(1,"",20,"active",null, "description", 10.0f, 1.0f);
		service.setRoomServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Name of room can't be empty")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Status_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(1,"Panorama",20,"wrong status",null, "description", 10.0f, 1.0f);
		service.setRoomServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid Argument for status")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Capacity_LT_1() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(1,"Panorama",0,"active",null, "description", 10.0f, 1.0f);
		service.setRoomServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Capacity can't be less than 1")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Price_Per_Day_LT_0() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(1,"Panorama",20,"active",null, "description", -100.0f, 1.0f);
		service.setRoomServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid value for Price Per Day.")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Price_Per_Hour_Is_LT_0() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(1,"Panorama",20,"active",null, "description", 10.0f, -100f);
		service.setRoomServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid value for Price Per Hour.")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	
	@Test
	public void updateRoomById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomServiceImpl service = new RoomServiceImpl();
		
		Room toBeCopied = new Room(100,"New Panorama",20,"active",null, "description", 10.0f, 1.0f);
		
		Optional<Room> found = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(found);
		service.setRoomServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.updateRoomById(1, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Room not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(1);
		Mockito.verify(mockDao, Mockito.never()).save(any(Room.class));
	}


}