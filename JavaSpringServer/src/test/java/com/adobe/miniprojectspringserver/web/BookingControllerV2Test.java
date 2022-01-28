







import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebMvcTest({BookingControllerV2.class})
@AutoConfigureMockMvc(addFilters = false) 
public class BookingControllerV2Test {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before()
	public void setup()
	{
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@MockBean
	BookingService mockService;
	
	@MockBean
	EmailService emailService;
	
	private class JsonIgnoreAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> clazz) {
	        return clazz.getAnnotation(JsonIgnore.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(JsonIgnore.class) != null;
	    }
	}

	
// Tests for getAllBookingsV2
	@Test
	public void getAllBookingsV2_Returns_Entire_List_Of_Bookings_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		returned.add(booking1);
		returned.add(booking2);
		returned.add(booking3);
		returned.add(booking4);
		returned.add(booking5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned).replace("[105,109,97,103,101,49]", "\"image1\"");
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings").param("to", "5").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType).equals(gson.fromJson(content.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType)));
		
	}	
	
	@Test
	public void getAllBookingsV2_Returns_Correct_Range_Of_List_Of_Bookings_In_Descending_Order_Of_Total() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		returned.add(booking4);
		returned.add(booking3);
		returned.add(booking2);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned).replace("[105,109,97,103,101,49]", "\"image1\"");
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("sort", "total").param("from", "2").param("to", "4").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType).equals(gson.fromJson(content.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType)));
	}	
	
	@Test
	public void getAllBookingsV2_Returns_Correct_Range_Of_List_Of_Bookings_In_Descending_Order_Of_Total_When_To_Parameter_GT_Number_Of_Bookings() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		returned.add(booking3);
		returned.add(booking2);
		returned.add(booking1);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned).replace("[105,109,97,103,101,49]", "\"image1\"");
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("sort", "total").param("from", "3").param("to", "400").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType).equals(gson.fromJson(content.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType)));	
	}
	
	@Test
	public void getAllBookingsV2_Returns_Empty_List_Of_Bookings_When_From_Parameter_GT_Number_Of_Bookings() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("from", "300").param("to", "400")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson, bookingListType).equals(gson.fromJson(content, bookingListType)));
	}
	
	@Test
	public void getAllBookingsV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("from", "300000").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals("From should be LTEQ To", content);
	}
	
	@Test
	public void getAllBookingsV2_Returns_List_Of_Bookings_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		returned.add(booking1);
		returned.add(booking2);
		returned.add(booking3);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned).replace("[105,109,97,103,101,49]", "\"image1\"");
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("sort", "total").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType).equals(gson.fromJson(content.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType)));
	}	
	
	@Test
	public void getAllBookingsV2_Returns_List_Of_Bookings_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		Booking booking1 = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking2 = new Booking(2, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking3 = new Booking(3, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 100, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking4 = new Booking(4, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 1000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking booking5 = new Booking(5, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10000, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());		
		List<Booking> savedList = new ArrayList<Booking>();
		savedList.add(booking1);
		savedList.add(booking2);
		savedList.add(booking3);
		savedList.add(booking4);
		savedList.add(booking5);
		
		List<Booking> returned = new ArrayList<Booking>();
		returned.add(booking2);
		returned.add(booking3);
		returned.add(booking4);
		returned.add(booking5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned).replace("[105,109,97,103,101,49]", "\"image1\"");
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings")
				.param("sort", "total").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
		assertTrue(gson.fromJson(returnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType).equals(gson.fromJson(content.replace("\"image1\"", "[105,109,97,103,101,49]"), bookingListType)));
	}	
		
		
// Tests for getBookingById
	@Test
	public void getBookingById_Returns_Booking_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Booking dataReturned = new Booking(id, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String dataReturnedJson = gson.toJson(dataReturned).replace("[105,109,97,103,101,49]", "\"image1\"");
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(gson.fromJson(response.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class).equals(gson.fromJson(dataReturnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class)));
	}	
	
	@Test
	public void getBookingById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Booking not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}
	
// Tests for addBooking
	@Test
	public void addBooking_Returns_Added_Booking_And_Header_And_HTTP_OK_When_Booking_Is_Valid() throws Exception{
		//Arrange
		Booking toBeAdded = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking addedWithAutoIncrementedId = new Booking(1, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded).replace("[105,109,97,103,101,49]", "\"image1\"");
		String addedWithAutoIncrementedIdJson = gson.toJson(addedWithAutoIncrementedId);
		Mockito.when(mockService.addNewBooking(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/bookings").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseBooking = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(gson.fromJson(responseBooking.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class).equals(gson.fromJson(addedWithAutoIncrementedIdJson, Booking.class)));
		//assertTrue(responseHeader.equals(""+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	public void addBooking_Returns_HTTP_BAD_REQUEST_And_Message_When_Booking_Is_Invalid() throws Exception {
		//Arrange
		Booking toBeAdded = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), null, new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded).replace("[105,109,97,103,101,49]", "\"image1\"");
		String exceptionMessage = "Booking client can't be null";
		Mockito.when(mockService.addNewBooking(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/bookings").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
// Tests for deleteBookingById
	@Test
	public void deleteBookingById_Returns_Booking_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Booking dataReturned = new Booking(id, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned).replace("[105,109,97,103,101,49]", "\"image1\"");
		Mockito.when(mockService.removeBooking(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/bookings/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseBooking = result.getResponse().getContentAsString();

		// Assert
		assertTrue(gson.fromJson(responseBooking.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class).equals(gson.fromJson(dataReturnedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class)));
	}	
	
	@Test
	public void deleteBookingById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Booking not found!!";
		Mockito.when(mockService.removeBooking(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/bookings/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	
// Tests for editBookingById
	@Test
	public void editBookingById_Returns_Booking_And_HTTP_OK_When_Id_And_Booking_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Booking saved = new Booking(id, "2020-09-10", 12, 14, 10, "pending", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking toBeSaved = new Booking(id, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		String toBeSavedJson = gson.toJson(toBeSaved).replace("[105,109,97,103,101,49]", "\"image1\"");
		Mockito.when(mockService.removeBooking(id)).thenReturn(saved);
		Mockito.when(mockService.addNewBooking(ArgumentMatchers.any(Booking.class))).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseBooking = result.getResponse().getContentAsString();

		// Assert
		assertTrue(gson.fromJson(responseBooking.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class).equals(gson.fromJson(toBeSavedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class)));
	}
	
	@Test
	public void editBookingById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Booking toBeCopied = new Booking(1000, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		String exceptionMessage = "Booking not found!!";
		Mockito.when(mockService.removeBooking(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
				.andExpect(MockMvcResultMatchers.status().is(404))
			
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		System.out.println(response);
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	public void editBookingById_Returns_HTTP_BAD_REQUEST_And_Message_When_Booking_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Booking saved = new Booking(id, "2020-09-10", 12, 14, 10, "pending", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking toBeCopied = new Booking(id, "2020-09-10", 12, 14, 10, "wrong_payment", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		String exceptionMessage = "Invalid Payment Method";
		Mockito.when(mockService.removeBooking(id)).thenReturn(saved);
		Mockito.when(mockService.addNewBooking(ArgumentMatchers.any(Booking.class))).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

// Tests for editBookingStatusById
	@Test
	public void editBookingStatusById_Returns_Booking_And_HTTP_OK_When_Id_And_Booking_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Booking toBeCopied = new Booking(id, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved  = new Booking(id, "2020-09-10", 12, 14, 10, "pending"  , "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		Mockito.when(mockService.editBookingById(id, toBeCopied)).thenReturn(toBeCopied);
		Mockito.when(mockService.findById(id)).thenReturn(saved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/status/{id}/confirmed", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseBooking = result.getResponse().getContentAsString();

		// Assert
		assertTrue(gson.fromJson(responseBooking.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class).equals(gson.fromJson(toBeCopiedJson.replace("\"image1\"", "[105,109,97,103,101,49]"), Booking.class)));
	}
	
	@Test
	public void editBookingStatusById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Booking toBeCopied = new Booking(id, "2020-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		String exceptionMessage = "Booking not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/status/{id}/confirmed", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	public void editBookingStatusById_Returns_HTTP_BAD_REQUEST_And_Message_When_Status_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Booking toBeCopied = new Booking(id, "2020-09-10", 12, 14, 10, "wrong_status", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Booking saved  = new Booking(id, "2020-09-10", 12, 14, 10, "pending"  , "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(1,"TitleE","image1".getBytes()), new Room(1,"Panorama",20,"active","image1".getBytes(), "description", 10.0f, 1.0f), new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[105,109,97,103,101,49]", "\"image1\"");
		String exceptionMessage = "Booking client can't be null";
		Mockito.when(mockService.editBookingById(id, toBeCopied)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		Mockito.when(mockService.findById(id)).thenReturn(saved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/bookings/status/{id}/wrong_status", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

// Tests for countRoomByIdV1
	@Test
	public void countRoomByIdV1_Returns_Number_Of_Rooms() throws Exception{
		//Arrange
		int id = 5;
		long num = 2l;
		Mockito.when(mockService.countBookedRoomByRoomId(id)).thenReturn(2l);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings/countroom/{id}", id).contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(Long.parseLong(response) == num);
	}
	
// Tests for getBookingSlots
	@Test
	public void getSlotBookings_Returns_List_Of_Pairs_Of_FromHour_And_ToHour() throws Exception{
		//Arrange
		
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
		
		Gson gson = new Gson();
		
		Mockito.when(mockService.findSlotBookings("2021-09-10", 5)).thenReturn(toBeReturned);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings/bookingslots/2021-09-10/5").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseBooking = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(responseBooking.equals(gson.toJson(toBeReturned)));
	}
	
// Tests for GetVariousCountsOfBookingsV1
	@Test
	public void GetVariousCountsOfBookingsV1_Returns_List_Of_Integers() throws Exception{
		//Arrange
		
		Room room1 = new Room(), room2 = new Room();
		room1.setId(5);
		room2.setId(10);
		Date date = new Date();
		@SuppressWarnings("deprecation")
		Date past_date = new Date(2020, 9, 10);
		Booking saved1 = new Booking(1, "2021-09-10", 2, 4, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		saved1.setRegDate(date);
		Booking saved2 = new Booking(2, "2000-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		saved2.setRegDate(date);
		Booking saved3 = new Booking(3, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room1, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		saved3.setRegDate(past_date);
		Booking saved4 = new Booking(4, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		saved4.setRegDate(past_date);
		Booking saved5 = new Booking(5, "2021-09-10", 12, 14, 10, "confirmed", "Cash", 10, 10, 10, 10, 10, 10, 10, new RoomLayout(),room2, new Client(), new ArrayList<BookingEquipments>(), new ArrayList<BookingSnacks>());
		saved5.setRegDate(past_date);
		List<Booking> savedBookings = new ArrayList<Booking>();
		savedBookings.add(saved1);
		savedBookings.add(saved2);
		savedBookings.add(saved3);
		savedBookings.add(saved4);
		savedBookings.add(saved5);
			
		Gson gson = new Gson();
		ArrayList<Integer> toBeReturned = new ArrayList<>();
		toBeReturned.add(5);
		toBeReturned.add(2);
		
		Mockito.when(mockService.findAll()).thenReturn(savedBookings);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/bookings/countofbookingstoday").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String response = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(response.equals(gson.toJson(toBeReturned)));
	}
}
