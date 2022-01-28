










import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebMvcTest({RoomControllerV2.class})
@AutoConfigureMockMvc(addFilters = false) 

class RoomControllerV2Test {

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
	

// Tests for getAllRoomsV2
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_Entire_List_Of_Rooms_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		returned.add(room1);
		returned.add(room2);
		returned.add(room3);
		returned.add(room4);
		returned.add(room5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_Correct_Range_Of_List_Of_Rooms_In_Descending_Order_Of_Name() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		returned.add(room2);
		returned.add(room3);
		returned.add(room4);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("sort", "name").param("from", "2").param("to", "4").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_Correct_Range_Of_List_Of_Rooms_In_Descending_Order_Of_Name_When_To_Parameter_GT_Number_Of_Rooms() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		returned.add(room3);
		returned.add(room4);
		returned.add(room5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("sort", "name").param("from", "3").param("to", "400").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
	}
	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_Empty_List_Of_Rooms_When_From_Parameter_GT_Number_Of_Rooms() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("from", "300").param("to", "400")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
	}
//	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("from", "300000").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals("From should be LTEQ To", content);
	}
	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_List_Of_Rooms_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		returned.add(room1);
		returned.add(room2);
		returned.add(room3);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("sort", "price").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomsV2_Returns_List_Of_Rooms_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		Room room1 = new Room(1,"NameE",2,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room2 = new Room(2,"NameD",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room3 = new Room(3,"NameC",200,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room4 = new Room(4,"NameB",2000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Room room5 = new Room(5,"NameA",20000,"active","image".getBytes(), "description", 10.0f, 1.0f);
		List<Room> savedList = new ArrayList<Room>();
		savedList.add(room1);
		savedList.add(room2);
		savedList.add(room3);
		savedList.add(room4);
		savedList.add(room5);
		
		List<Room> returned = new ArrayList<Room>();
		returned.add(room2);
		returned.add(room3);
		returned.add(room4);
		returned.add(room5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms")
				.param("sort", "price").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<Room> room = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, Room.class));
		assertTrue(room.equals(returned));
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
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
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}/layouts", id).accept(MediaType.APPLICATION_JSON))
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
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}/layouts", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}/bookings", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/rooms/{id}/bookings", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(content.contains(exceptionMessage));
	}
	
	// Tests for addRoom
	class JsonIgnoreAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> clazz) {
	        return clazz.getAnnotation(JsonIgnore.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(JsonIgnore.class) != null;
	    }
	}
	
	@Test
	@WithMockUser
	public void addRoom_Returns_Added_Room_And_Header_And_HTTP_OK_When_Room_Is_Valid() throws Exception{
		
		//Arrange
		Room toBeAdded = new Room(1000,"Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Room addedWithAutoIncrementedId = new Room(5,"Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
//		Set<RoomLayout> layouts = new HashSet<RoomLayout>(); 
//		layouts.add(new RoomLayout(1, "title", "image".getBytes()));
//		toBeAdded.setLayouts(layouts);
//		addedWithAutoIncrementedId.setLayouts(layouts);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String toBeAddedJson = (gson.toJson(toBeAdded)).replace("[]", "\"\"");
		Mockito.when(mockService.addNewRoom(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/rooms").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseRoom = result.getResponse().getContentAsString();
		
		// Assert
		Mockito.verify(mockService).addNewRoom(toBeAdded);
		Room room = new ObjectMapper().readValue(responseRoom, Room.class);
		assertTrue(room.equals(addedWithAutoIncrementedId));
		//assertTrue(responseHeader.equals(""+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	@WithMockUser
	public void addRoom_Returns_HTTP_BAD_REQUEST_And_Message_When_Room_Is_Invalid() throws Exception {
		//Arrange
		Room toBeAdded = new Room(1000,"",20,"active","".getBytes(), "description", 10.0f, 1.0f);
//		Set<RoomLayout> layouts = new HashSet<RoomLayout>(); 
//		layouts.add(new RoomLayout(1, "title", "image".getBytes()));
//		toBeAdded.setLayouts(layouts);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded).replace("[]", "\"\"");
		String exceptionMessage = "Name of room can't be empty";
		Mockito.when(mockService.addNewRoom(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/rooms").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	
	// Tests for deleteRoomById
	@Test
	@WithMockUser
	public void deleteRoomById_Returns_Room_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Room dataReturned = new Room(1,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
		Mockito.when(mockService.removeRoom(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseRoom = result.getResponse().getContentAsString();

		// Assert
		Room room = new ObjectMapper().readValue(responseRoom, Room.class);
		assertTrue(room.equals(dataReturned));
	}	
	
	@Test
	@WithMockUser
	public void deleteRoomById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Room not found!!";
		Mockito.when(mockService.removeRoom(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/rooms/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// Tests for updateRoomById
	@Test
	@WithMockUser
	public void updateRoomById_Returns_Room_And_HTTP_OK_When_Id_And_Room_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Room toBeCopied = new Room(1000,"New Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Room toBeSaved = new Room(id,"Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");
		Mockito.when(mockService.updateRoomById(id, toBeCopied)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseRoom = result.getResponse().getContentAsString();

		// Assert
		Room room = new ObjectMapper().readValue(responseRoom, Room.class);
		assertTrue(room.equals(toBeSaved));
	}
	
	@Test
	@WithMockUser
	public void updateRoomById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Room toBeCopied = new Room(1000,"New Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");
		String exceptionMessage = "Room not found!!";
		Mockito.when(mockService.updateRoomById(id, toBeCopied)).thenThrow(new IllegalArgumentNotFoundException	(exceptionMessage));
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void updateRoomById_Returns_HTTP_BAD_REQUEST_And_Message_When_Room_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Room toBeCopied = new Room(1000,"",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");;
		String exceptionMessage = "Name of room can't be empty";
		Mockito.when(mockService.updateRoomById(id, toBeCopied)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
// Tests for editRoomStatusById
	@Test
	@WithMockUser
	public void editRoomStatusById_Returns_Room_And_HTTP_OK_When_Id_And_Status_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Room toBeSaved = new Room(id,"Panorama",20,"inactive","".getBytes(), "description", 10.0f, 1.0f);
		Room saved = new Room(id,"Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		Mockito.when(mockService.findById(id)).thenReturn(saved);
		Mockito.when(mockService.updateRoomById(id, toBeSaved)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/status/{id}/inactive", id).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseRoom = result.getResponse().getContentAsString();

		// Assert
		Room room = new ObjectMapper().readValue(responseRoom, Room.class);
		assertTrue(room.equals(toBeSaved));
	}
	
	@Test
	@WithMockUser
	public void editRoomStatusById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		String exceptionMessage = "Room not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException	(exceptionMessage));
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/status/{id}/inactive", id).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void editRoomStatusById_Returns_HTTP_BAD_REQUEST_And_Message_When_Status_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Room toBeSaved = new Room(id,"Panorama",20,"wrong_status","".getBytes(), "description", 10.0f, 1.0f);
		Room saved = new Room(id,"Panorama",20,"active","".getBytes(), "description", 10.0f, 1.0f);
		String exceptionMessage = "Invalid Argument for status";
		Mockito.when(mockService.findById(id)).thenReturn(saved);
		Mockito.when(mockService.updateRoomById(id, toBeSaved)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/rooms/status/{id}/wrong_status", id).contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

}
