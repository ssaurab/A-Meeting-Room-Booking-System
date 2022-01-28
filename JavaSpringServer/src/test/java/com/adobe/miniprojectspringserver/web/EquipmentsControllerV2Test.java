





import com.google.gson.Gson;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebMvcTest({EquipmentsControllerV2.class})
@AutoConfigureMockMvc(addFilters = false)
class EquipmentsControllerV2Test {
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

	
// Tests for getAllEquipmentsV2
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_Entire_List_Of_Equipments_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		returned.add(equipment1);
		returned.add(equipment2);
		returned.add(equipment3);
		returned.add(equipment4);
		returned.add(equipment5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_Correct_Range_Of_List_Of_Equipments_In_Descending_Order_Of_Title() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		returned.add(equipment2);
		returned.add(equipment3);
		returned.add(equipment4);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
				.param("sort", "title").param("from", "2").param("to", "4").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_Correct_Range_Of_List_Of_Equipments_In_Descending_Order_Of_Title_When_To_Parameter_GT_Number_Of_Equipments() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		returned.add(equipment3);
		returned.add(equipment4);
		returned.add(equipment5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
				.param("sort", "title").param("from", "3").param("to", "400").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}
	
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_Empty_List_Of_Equipments_When_From_Parameter_GT_Number_Of_Equipments() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
				.param("from", "300").param("to", "400")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}
	
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
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
	public void getAllEquipmentsV2_Returns_List_Of_Equipments_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		returned.add(equipment1);
		returned.add(equipment2);
		returned.add(equipment3);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
				.param("sort", "price").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllEquipmentsV2_Returns_List_Of_Equipments_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		Equipment equipment1 = new Equipment(1, "titleE", true, "perHour", (float)1.0);
		Equipment equipment2 = new Equipment(2, "titleD", true, "perHour", (float)10.0);
		Equipment equipment3 = new Equipment(3, "titleC", true, "perHour", (float)100.0);
		Equipment equipment4 = new Equipment(4, "titleB", true, "perHour", (float)1000.0);
		Equipment equipment5 = new Equipment(5, "titleA", true, "perHour", (float)10000.0);
		List<Equipment> savedList = new ArrayList<Equipment>();
		savedList.add(equipment1);
		savedList.add(equipment2);
		savedList.add(equipment3);
		savedList.add(equipment4);
		savedList.add(equipment5);
		
		List<Equipment> returned = new ArrayList<Equipment>();
		returned.add(equipment2);
		returned.add(equipment3);
		returned.add(equipment4);
		returned.add(equipment5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments")
				.param("sort", "price").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/equipments").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/equipments").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/equipments/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/equipments/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
}
