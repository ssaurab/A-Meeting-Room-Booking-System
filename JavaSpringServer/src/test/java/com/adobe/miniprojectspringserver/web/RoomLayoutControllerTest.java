

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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

@RunWith(SpringRunner.class)
@WebMvcTest({RoomLayoutController.class})
@AutoConfigureMockMvc(addFilters = false) 
public class RoomLayoutControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
    RoomLayoutService mockService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before()
	public void setup()
	{
	    //Init MockMvc Object and build
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	// Tests for getAllRoomLayouts
	@Test
	@WithMockUser
	public void getAllRoomLayouts_Returns_List_Of_RoomLayouts_And_HTTP_OK() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"Panorama","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(1,"Panorama","image".getBytes());

		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/roomlayouts").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(savedList));
	}
	
// Tests for getRoomLayoutById
	@Test
	@WithMockUser
	public void getRoomLayoutById_Returns_RoomLayout_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		RoomLayout dataReturned = new RoomLayout(id,"Panorama","image".getBytes());

		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();
		// Assert
		RoomLayout roomLayout = new ObjectMapper().readValue(response, RoomLayout.class);
		assertTrue(roomLayout.equals(dataReturned));
	}
	
	@Test
	@WithMockUser
	public void getRoomLayoutById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "RoomLayout not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}
	
// Tests for getAllRoomsAvailableForRoomLayoutByID
//	@Test
//	@WithMockUser
//	public void getAllRoomsAvailableForRoomLayoutByID_Returns_List_Of_Rooms_And_HTTP_OK() throws Exception{
//		//Arrange
//		int id=1;
//		Room room1 = new Room(id,"Panorama",20,"active","image".getBytes(), "description", 10.0f, 1.0f);
//		Set<RoomLayout> layouts = new HashSet<RoomLayout>();
//		layouts.add(new RoomLayout(1, "title", "image".getBytes()));
//		layouts.add(new RoomLayout(3, "title2", "image".getBytes()));
//		room1.setLayouts(layouts);
//		
//		 
//		Mockito.when(mockService.getAllRoomLayoutsAvailableForThisRoom(id)).thenReturn(layouts);
//		
//		//Act and Assert
//		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}/layouts", id).accept(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().is(200))
//			.andReturn();
//		String content = result.getResponse().getContentAsString();
//		
//		// Assert
//		Set<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(Set.class, RoomLayout.class));
//		assertTrue(roomLayout.equals(layouts));
//	}
}
