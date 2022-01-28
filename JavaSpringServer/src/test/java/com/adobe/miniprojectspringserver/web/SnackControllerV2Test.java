

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;





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

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest({SnackControllerV2.class})
@AutoConfigureMockMvc(addFilters = false) 
class SnackControllerV2Test {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
    SnackService mockService;
	
	
// Tests for getAllSnacksV2
	@Test
	@WithMockUser
	public void getAllSnacksV2_Returns_Entire_List_Of_Snacks_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		returned.add(snack1);
		returned.add(snack2);
		returned.add(snack3);
		returned.add(snack4);
		returned.add(snack5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllSnacksV2_Returns_Correct_Range_Of_List_Of_Snacks_In_Descending_Order_Of_Title() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		returned.add(snack2);
		returned.add(snack3);
		returned.add(snack4);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
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
	public void getAllSnacksV2_Returns_Correct_Range_Of_List_Of_Snacks_In_Descending_Order_Of_Name_When_To_Parameter_GT_Number_Of_Snacks() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		returned.add(snack3);
		returned.add(snack4);
		returned.add(snack5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
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
	public void getAllSnacksV2_Returns_Empty_List_Of_Snacks_When_From_Parameter_GT_Number_Of_Snacks() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
				.param("sort", "title").param("from", "300").param("to", "400")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}
//	
	@Test
	@WithMockUser
	public void getAllSnacksV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
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
	public void getAllSnacksV2_Returns_List_Of_Snacks_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		returned.add(snack5);
		returned.add(snack4);
		returned.add(snack3);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
				.param("sort", "title").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllSnacksV2_Returns_List_Of_Snacks_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "titleE", (float)1.0);
		Snack snack2 = new Snack(2, "titleD", (float)10.0);
		Snack snack3 = new Snack(3, "titleC", (float)100.0);
		Snack snack4 = new Snack(4, "titleB", (float)1000.0);
		Snack snack5 = new Snack(5, "titleA", (float)10000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		savedList.add(snack3);
		savedList.add(snack4);
		savedList.add(snack5);
		
		List<Snack> returned = new ArrayList<Snack>();
		returned.add(snack2);
		returned.add(snack3);
		returned.add(snack4);
		returned.add(snack5);
		Gson gson = new Gson();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks")
				.param("sort", "price").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	// tests for getSnacksByID
	@Test
	@WithMockUser
	public void getSnackById_Returns_Snack_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Snack dataReturned = new Snack(id, "title", (float)1000.0);
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void getSnackById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Snack not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/snacks/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}
	
	// tests for addSnacks
	@Test
	@WithMockUser
	public void addSnack_Returns_Added_Snack_And_Header_And_HTTP_OK_When_Snack_Is_Valid() throws Exception{
		//Arrange
		Snack toBeAdded = new Snack(0, "title", (float)1000.0);
		Snack addedWithAutoIncrementedId = new Snack(5, "title", (float)1000.0);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String addedWithAutoIncrementedIdJson = gson.toJson(addedWithAutoIncrementedId);
		Mockito.when(mockService.addNewSnack(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/snacks").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseSnack = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(responseSnack.equals(addedWithAutoIncrementedIdJson));
		//assertTrue(responseHeader.equals(""+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	@WithMockUser
	public void addSnack_Returns_HTTP_BAD_REQUEST_And_Message_When_Snack_Is_Invalid() throws Exception {
		//Arrange
		Snack toBeAdded = new Snack(0, "", (float)1000.0);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String exceptionMessage = "Name of snack is required";
		Mockito.when(mockService.addNewSnack(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/snacks").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// tests for updateSnacks
	@Test
	@WithMockUser
	public void updateById_Returns_Snack_And_HTTP_OK_When_Id_And_Snack_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Snack toBeCopied = new Snack(500, "new title", (float)1000.0);
		Snack toBeSaved = new Snack(id, "title", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String toBeSavedJson = gson.toJson(toBeSaved);
		Mockito.when(mockService.updateById(toBeCopied, id)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/snacks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseSnack = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseSnack.equals(toBeSavedJson));
	}
	
	@Test
	@WithMockUser
	public void editSnackById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Snack toBeCopied = new Snack(500, "title", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Snack not found!!";
		Mockito.when(mockService.updateById(toBeCopied, id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/snacks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void editSnackById_Returns_HTTP_BAD_REQUEST_And_Message_When_Snack_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Snack toBeCopied = new Snack(500, "", (float)1000.0);
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Name of snack is required";
		Mockito.when(mockService.updateById(toBeCopied, id)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/snacks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// tests for deleteSnacks
	@Test
	@WithMockUser
	public void deleteSnackById_Returns_Snack_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Snack dataReturned = new Snack(id, "title", (float)1000.0);
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.removeSnack(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/snacks/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseSnack = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseSnack.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void deleteSnackById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Snack not found!!";
		Mockito.when(mockService.removeSnack(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/snacks/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
}
