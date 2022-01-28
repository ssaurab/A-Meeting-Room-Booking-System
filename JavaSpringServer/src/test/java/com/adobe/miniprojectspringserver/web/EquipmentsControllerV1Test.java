

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;






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

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest({EquipmentsControllerV1.class})
@AutoConfigureMockMvc(addFilters = false)
class EquipmentsControllerV1Test {
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/equipments").accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}
	
	// tests for addEquipments
	@Test
	@WithMockUser
	public void addEquipment_Returns_Added_Equipment_And_Header_And_HTTP_OK_When_Equipment_Is_Valid() throws Exception{
		//Arrange
		Equipment toBeAdded = new Equipment(0, "title", true, "perHour", (float)1000.0);
		Equipment addedWithAutoIncrementedId = new Equipment(0, "title", true, "perHour", (float)1000.0);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String addedWithAutoIncrementedIdJson = gson.toJson(addedWithAutoIncrementedId);
		Mockito.when(mockService.addNewEquipment(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V1/equipments").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseEquipment = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(responseEquipment.equals(addedWithAutoIncrementedIdJson));
		//assertTrue(responseHeader.equals("//"+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	@WithMockUser
	public void addEquipment_Returns_HTTP_BAD_REQUEST_And_Message_When_Equipment_Is_Invalid() throws Exception {
		//Arrange
		Equipment toBeAdded = new Equipment(0, "title", true, "wrong pricetype", (float)1000.0);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String exceptionMessage = "Invalid Parameter to PriceType";
		Mockito.when(mockService.addNewEquipment(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V1/equipments").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// tests for updateEquipments
	@Test
	@WithMockUser
	public void updateById_Returns_Equipment_And_HTTP_OK_When_Id_And_Equipment_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Equipment toBeCopied = new Equipment(500, "new title", true, "perHour", (float)1000.0);
		Equipment toBeSaved = new Equipment(id, "title", true, "perHour", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String toBeSavedJson = gson.toJson(toBeSaved);
		Mockito.when(mockService.updateById(toBeCopied, id)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V1/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseEquipment = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseEquipment.equals(toBeSavedJson));
	}
	
	@Test
	@WithMockUser
	public void editEquipmentById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Equipment toBeCopied = new Equipment(500, "title", true, "perHour", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Equipment not found!!";
		Mockito.when(mockService.updateById(toBeCopied, id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V1/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void editEquipmentById_Returns_HTTP_BAD_REQUEST_And_Message_When_Equipment_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Equipment toBeCopied = new Equipment(500, "title", true, "wrong pricetype", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Invalid Parameter to PriceType";
		Mockito.when(mockService.updateById(toBeCopied, id)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V1/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// tests for deleteEquipments
	@Test
	@WithMockUser
	public void deleteEquipmentById_Returns_Equipment_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Equipment dataReturned = new Equipment(id, "title", true, "wrong pricetype", (float)1000.0);
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.removeEquipment(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V1/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseEquipment = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseEquipment.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void deleteEquipmentById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Equipment not found!!";
		Mockito.when(mockService.removeEquipment(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V1/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

}
