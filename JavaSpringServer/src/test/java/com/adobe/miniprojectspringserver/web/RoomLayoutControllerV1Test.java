



import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebMvcTest({RoomLayoutControllerV1.class})
@AutoConfigureMockMvc(addFilters = false)  
public class RoomLayoutControllerV1Test {

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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/roomlayouts").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(savedList));
	}

}
