

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




import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest({EquipmentsController.class})
@AutoConfigureMockMvc(addFilters = false)
public class EquipmentsControllerTest {
@Autowired
	
	MockMvc mockMvc;
	
	@MockBean
    EquipmentService mockService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before()
	public void setup()
	{
	    //Init MockMvc Object and build
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

// Tests for getAllEquipmentsV1
	@Test
	@WithMockUser
	public void getAllEquipmentsV1_Returns_List_Of_Equipments() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "title", true, "perHour", (float)1000.0);
		Equipment equipment2 = new Equipment(2, "title", true, "perHour", (float)1000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		
		Gson gson = new Gson();
		String savedListJson = gson.toJson(savedList);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/equipments").accept(MediaType.APPLICATION_JSON))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(savedListJson, content);
	}	
	
// tests for getEquipmentsByID
	@Test
	@WithMockUser
	public void getEquipmentById_Returns_Equipment_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Equipment dataReturned = new Equipment(0, "title", true, "perHour", (float)1000.0);
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void getEquipmentById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Equipment not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}

}
