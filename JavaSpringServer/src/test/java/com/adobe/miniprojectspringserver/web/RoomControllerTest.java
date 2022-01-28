

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;









import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest({RoomController.class})
@AutoConfigureMockMvc(addFilters = false)
public class RoomControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
    RoomService mockService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before()
	public void setup()
	{
	    //Init MockMvc Object and build
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	// Tests for getAllRooms
	@Test
	@WithMockUser
	public void getAllRooms_Returns_List_Of_Rooms_And_HTTP_OK() throws Exception{
		//Arrange
		Room room1 = new Room(1,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(1,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Set<RoomLayout> layouts = new HashSet<RoomLayout>();
		layouts.add(new RoomLayout(1, "title", "image".getBytes()));
		room1.setLayouts(layouts);
		room2.setLayouts(layouts);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(savedList));
//		assertEquals(savedListJson, content);
	}
	
// Tests for getRoomById
	@Test
	@WithMockUser
	public void getRoomById_Returns_Room_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Room dataReturned = new Room(id,"Panorama",20,"active","hi".getBytes(), "description", 10.0f, 1.0f);
		Set<RoomLayout> layouts = new HashSet<RoomLayout>(); 
		layouts.add(new RoomLayout(1, "title", "".getBytes()));
		dataReturned.setLayouts(layouts);
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();
		
		// Assert
		Room room = new ObjectMapper().readValue(response, Room.class);
		assertTrue(room.equals(dataReturned));
	}
	
	@Test
	@WithMockUser
	public void getRoomById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Room not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}

// Tests for getAllLayoutsAvailableForRoomByID
	@Test
	@WithMockUser
	public void getAllLayoutsAvailableForRoomByID_Returns_List_Of_RoomLayouts_And_HTTP_OK() throws Exception{
		//Arrange
		int id=1;
		Room room1 = new Room(id,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Set<RoomLayout> layouts = new HashSet<RoomLayout>();
		layouts.add(new RoomLayout(1, "title", "image".getBytes()));
		layouts.add(new RoomLayout(3, "title2", "image".getBytes()));
		room1.setLayouts(layouts);
		
		 
		Mockito.when(mockService.getAllRoomLayoutsAvailableForThisRoom(id)).thenReturn(layouts);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}/layouts", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Set<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(Set.class, RoomLayout.class));
		assertTrue(roomLayout.equals(layouts));
	}
	
	@Test
	@WithMockUser
	public void getAllLayoutsAvailableForRoomByID_Returns_HTTP_NOT_FOUND_If_Id_Is_Invalid() throws Exception{
		//Arrange
		int id=1;
		String exceptionMessage = "Room not found!!";
		
		Mockito.when(mockService.getAllRoomLayoutsAvailableForThisRoom(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}/layouts", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(content.contains(exceptionMessage));
	}
	
// Tests for getAllBookingDoneForRoomByID
	@Test
	@WithMockUser
	public void getAllBookingDoneForRoomByID_Returns_List_Of_Bookings_And_HTTP_OK() throws Exception{
		//Arrange
		int id=1;
		Room room1 = new Room(id,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(id,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		RoomLayout layout = new RoomLayout(1, "title", "image".getBytes());
		Set<Booking> savedBookings = new HashSet<Booking>();
		savedBookings.add(new Booking(2, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, layout, room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		savedBookings.add(new Booking(4, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, layout, room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		savedBookings.add(new Booking(6, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, layout, room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>()));
		room1.setBookings(savedBookings);
		
		 
		Mockito.when(mockService.getAllBookingOfThisRoom(id)).thenReturn(savedBookings);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}/bookings", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		
		Set<Booking> booking = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(Set.class, Booking.class));
		Gson gson = new Gson();
		assertTrue(gson.toJson(booking).equals(gson.toJson(savedBookings)));
	}
	
	@Test
	@WithMockUser
	public void getAllBookingDoneForRoomByID_Returns_HTTP_NOT_FOUND_If_Id_Is_Invalid() throws Exception{
		//Arrange
		int id=1;
		String exceptionMessage = "Room not found!!";
		
		Mockito.when(mockService.getAllBookingOfThisRoom(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}/bookings", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(content.contains(exceptionMessage));
	}
}
