




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
public class RoomLayoutServiceImplTest
{
    @Mock
    RoomLayoutRepository mockDao;

    @Test
    public void addNewRoomLayout_Must_Return_Added_Room_When_Room_Is_Valid() {
        //Arrange
        RoomLayout toBeAdded = new RoomLayout(0,"Circle",null);
        RoomLayout saved = new RoomLayout(0,"Circle",null);
        RoomLayout toBeAdded2 = new RoomLayout(0,"rectangle",null);
        RoomLayout saved2 = new RoomLayout(0,"rectangle",null);

        RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
        service.setRoomLayoutServiceDao(mockDao);
        Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
        Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
        //Act
        RoomLayout user = service.addNewRoomLayout(toBeAdded);
        RoomLayout user2 = service.addNewRoomLayout(toBeAdded2);
        //Assert
        assertEquals(2, Mockito.mockingDetails(mockDao).getInvocations().size());
//		Mockito.verify(mockDao, Mockito.times(2));
        assertSame(user, saved);
        assertSame(user2, saved2);
    }
    
    @Test
	public void addNewRoom_Must_Throw_Illegal_Argument_Exeption_When_Name_Is_Null() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		RoomLayout toBeAdded = new RoomLayout(0,"",null);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewRoomLayout(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Layout name can't be empty")) {
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
  	public void removeRoomLayout_Must_Return_Removed_RoomLayout_When_Id_Is_Valid() {
  		//Arrange
  		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
  		RoomLayout saved = new RoomLayout(1,"Panorama",null);
  		Set<Room> rooms = new HashSet<Room>();
  		saved.setRooms(rooms);
  		Optional<RoomLayout> returned = Optional.of(saved);
  		Mockito.when(mockDao.findById(1)).thenReturn(returned);
  		service.setRoomLayoutServiceDao(mockDao);
  		
  		//Act
  		RoomLayout roomLayout = service.removeRoomLayout(1);
  		
  		//Assert
  		Mockito.verify(mockDao).deleteById(1);;
  		assertSame(roomLayout, saved);
  	}
  	
  	@Test
 	public void removeRoom_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
 		//Arrange
 		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
 		Optional<RoomLayout> returned = Optional.empty();
 		Mockito.when(mockDao.findById(1)).thenReturn(returned);
 		service.setRoomLayoutServiceDao(mockDao);
 		
 		//Act and Assert
 	    try {
 	    	service.removeRoomLayout(1);
 	    	fail(); // Should not reach here
 	    }
 	    catch (IllegalArgumentException e) {
 	    	if(!e.getMessage().equals("RoomLayout not found!!")) {
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
  	public void findAll_Must_Return_List_Of_RoomLayouts() {
  		//Arrange
  		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
  		
  		RoomLayout roomLayout1 = new RoomLayout(1,"Panorama",null);
  		RoomLayout roomLayout2 = new RoomLayout(2,"Panorama 2",null);
  		
  		List<RoomLayout> list = new ArrayList<RoomLayout>();
  		list.add(roomLayout1);
  		list.add(roomLayout2);
  		
  		Mockito.when(mockDao.findAll()).thenReturn(list);
  		service.setRoomLayoutServiceDao(mockDao);
  		
  		//Act
  		List<RoomLayout> returned = service.findAll();
  		
  		//Assert
  		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
  		assertTrue(returned.get(0) == roomLayout1 && returned.get(1) == roomLayout2);
  	}
  
  	// Tests for getAllRoomAvailableForThisRoomLayout
  	@Test
	public void getAllRoomAvailableForThisRoomLayout_Must_Return_Rooms_When_Id_Is_Valid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		RoomLayout saved = new RoomLayout(1,"Panorama",null);
		Set<Room> roomSet = new HashSet<Room>();
		roomSet.add(new Room(2,"Panorama",20,"active",null, "description", 10.0f, 1.0f));
		roomSet.add(new Room(4,"Classroom",20,"active",null, "description", 10.0f, 1.0f));
		roomSet.add(new Room(6,"Banquet",20,"active",null, "description", 10.0f, 1.0f));
		saved.setRooms(roomSet);
		Optional<RoomLayout> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act
		Set<Room> setReturned = service.getAllRoomAvailableForThisRoomLayout(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertTrue(setReturned.equals(roomSet));
	}
	
	@Test
	public void getAllRoomAvailableForThisRoomLayout_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		Optional<RoomLayout> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.getAllRoomAvailableForThisRoomLayout(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("RoomLayout not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}
  	
	// Tests for findById
	@Test
	public void findById_Must_Return_RoomLayout_When_Id_Is_Valid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		RoomLayout saved = new RoomLayout(1,"Panorama",null);
		Optional<RoomLayout> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act
		RoomLayout roomLayout = service.findById(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertSame(roomLayout, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		Optional<RoomLayout> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("RoomLayout not found!!")) {
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
	public void updateRoomLayoutById_Must_Return_Edited_RoomLayout_When_Id_Is_Valid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		
		RoomLayout toBeCopied = new RoomLayout(100,"New Panorama",null);
		RoomLayout saved = new RoomLayout(1,"Panorama",null);
		RoomLayout toBeSaved = new  RoomLayout(1,"New Panorama",null);
		
		Optional<RoomLayout> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act
		RoomLayout returned = service.updateRoomLayoutById(saved.getId(), toBeCopied);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(RoomLayout.class));
		System.out.println(returned);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void updateRoomLayoutById_Must_Throw_Illegal_Argument_Exception_When_Name_Is_Null() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		
		RoomLayout toBeCopied = new RoomLayout(1,"",null);
		service.setRoomLayoutServiceDao(mockDao);

		//Act and Assert
	    try {
	    	service.updateRoomLayoutById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Layout name can't be empty")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void updateRoomLayoutById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		RoomLayoutServiceImpl service = new RoomLayoutServiceImpl();
		
		RoomLayout toBeCopied = new RoomLayout(100,"New Panorama",null);
		
		Optional<RoomLayout> found = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(found);
		service.setRoomLayoutServiceDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.updateRoomLayoutById(1, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("RoomLayout not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(1);
		Mockito.verify(mockDao, Mockito.never()).save(any(RoomLayout.class));
	}
}