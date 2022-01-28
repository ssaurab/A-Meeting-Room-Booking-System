

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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





import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest({ClientControllerV2.class})
public class ClientControllerV2Test {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before()
	public void setup()
	{
	    //Init MockMvc Object and build
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@MockBean
	ClientService mockService;
	
	// Tests for getAllClients
	@Test
	public void getAllClients_Returns_List_Of_Clients_And_HTTP_OK() throws Exception{
		//Arrange
		Client client1 = new Client(1, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client client2 = new Client(2, "Mrs.", "Jen", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		List<Client> savedList = new ArrayList<Client>();
		savedList.add(client1);
		savedList.add(client2);
		
		Gson gson = new Gson();
		String savedListJson = gson.toJson(savedList);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/clients").accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(content.equals(savedListJson));
	}	
	
	// Tests for getClientById
	@Test
	public void getClientById_Returns_Client_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Client dataReturned = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/clients/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String response = result.getResponse().getContentAsString();
		
		//Assert
		assertTrue(response.equals(dataReturnedJson));;
	}	
	
	@Test
	public void getClientById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Client not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/clients/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}

	// Tests for getClientById
	@Test
	public void addClient_Returns_Added_Client_And_Header_And_HTTP_OK_When_Client_Is_Valid() throws Exception{
		//Arrange
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client addedWithAutoIncrementedId = new Client(1, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String addedWithAutoIncrementedIdJson = gson.toJson(addedWithAutoIncrementedId);
		Mockito.when(mockService.addNewClient(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/clients").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseClient = result.getResponse().getContentAsString();
		
		// Assert
		assertTrue(responseClient.equals(addedWithAutoIncrementedIdJson));
		//assertTrue(responseHeader.equals("/V2/clients"));
	}
	
	@Test
	public void addClient_Returns_HTTP_BAD_REQUEST_And_Message_When_Client_Is_Invalid() throws Exception {
		//Arrange
		Client toBeAdded = new Client(1000, "Mr.", "John", "wrong email", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded);
		String exceptionMessage = "Invalid role email field(s) for Client argument";
		Mockito.when(mockService.addNewClient(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/clients").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// Tests for deleteClientById
	@Test
	public void deleteClientById_Returns_Client_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		Client dataReturned = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.removeClient(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/clients/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseClient = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseClient.equals(dataReturnedJson));
	}	
	
	@Test
	public void deleteClientById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "Client not found!!";
		Mockito.when(mockService.removeClient(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/clients/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

	// Tests for editClientById
	@Test
	public void editClientById_Returns_Client_And_HTTP_OK_When_Id_And_Client_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		Client toBeCopied = new Client(1000, "Mrs.", "Jen", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client toBeSaved = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String toBeSavedJson = gson.toJson(toBeSaved);
		Mockito.when(mockService.editClientById(id, toBeCopied)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/clients/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseClient = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseClient.equals(toBeSavedJson));
	}
	
	@Test
	public void editClientById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Client toBeCopied = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Client not found!!";
		Mockito.when(mockService.editClientById(id, toBeCopied)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/clients/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	public void editClientById_Returns_HTTP_BAD_REQUEST_And_Message_When_Client_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		Client toBeCopied = new Client(1000, "Mr.", "John", "wrong email", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Invalid role email field(s) for Client argument";
		Mockito.when(mockService.editClientById(id, toBeCopied)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/clients/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
}
