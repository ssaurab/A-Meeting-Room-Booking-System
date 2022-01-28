





import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration
@WebMvcTest({UserControllerV2.class})
public class UserControllerV2Test {
	
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
	UserService mockService;
	
	@MockBean
	BCryptPasswordEncoder passwordEncoder;
	
	class JsonIgnoreAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> clazz) {
	        return clazz.getAnnotation(JsonIgnore.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(JsonIgnore.class) != null;
	    }
	}

	
 // Tests for getAllUsersV2
	@Test
	@WithMockUser
	public void getAllUsersV2_Returns_Entire_List_Of_Users_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "John", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "John", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "John", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "John", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "John", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		returned.add(user1);
		returned.add(user2);
		returned.add(user3);
		returned.add(user4);
		returned.add(user5);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllUsersV2_Returns_Correct_Range_Of_List_Of_Users_In_Descending_Order_Of_Email() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "John", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "John", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "John", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "John", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "John", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		returned.add(user5);
		returned.add(user2);
		returned.add(user4);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
				.param("sort", "email").param("from", "2").param("to", "4").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllUsersV2_Returns_Correct_Range_Of_List_Of_Users_In_Descending_Order_Of_Name_When_To_Parameter_GT_Number_Of_Users() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "A", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "C", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "E", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "B", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "D", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		returned.add(user2);
		returned.add(user4);
		returned.add(user1);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
				.param("sort", "name").param("from", "3").param("to", "400").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}
	
	@Test
	@WithMockUser
	public void getAllUsersV2_Returns_Empty_List_Of_Users_When_From_Parameter_GT_Number_Of_Users() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "A", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "C", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "E", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "B", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "D", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
				.param("sort", "name").param("from", "300").param("to", "400")
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
	public void getAllUsersV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
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
	public void getAllUsersV2_Returns_List_Of_Users_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "John", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "John", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "John", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "John", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "John", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		returned.add(user1);
		returned.add(user4);
		returned.add(user2);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
				.param("sort", "email").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	@Test
	@WithMockUser
	public void getAllUsersV2_Returns_List_Of_Users_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "a@abc.com", "1234", "John", "1234567890", "active");
		User user2 = new User(2, "editor", "c@abc.com", "1234", "John", "1234567890", "inactive");
		User user3 = new User(3, "editor", "e@abc.com", "1234", "John", "1234567890", "inactive");
		User user4 = new User(4, "editor", "b@abc.com", "1234", "John", "1234567890", "inactive");
		User user5 = new User(5, "editor", "d@abc.com", "1234", "John", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		savedList.add(user3);
		savedList.add(user4);
		savedList.add(user5);
		
		List<User> returned = new ArrayList<User>();
		returned.add(user4);
		returned.add(user2);
		returned.add(user5);
		returned.add(user3);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String returnedJson = gson.toJson(returned);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users")
				.param("sort", "email").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(returnedJson, content);
	}	
	
	
	// Tests for getUserById
	@Test
	@WithMockUser
	public void getUserById_Returns_User_And_HTTP_OK_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User dataReturned = new User(id, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.findById(id)).thenReturn(dataReturned);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void getUserById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "User not found!!";
		Mockito.when(mockService.findById(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		//Act and Assert
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/users/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}

	// Tests for getUserById
	@Test
	@WithMockUser
	public void addUser_Returns_Added_User_And_Header_And_HTTP_OK_When_User_Is_Valid() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User toBeAdded = new User(0, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User addedWithAutoIncrementedId = new User(5, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String toBeAddedJson = gson.toJson(toBeAdded);
		String addedWithAutoIncrementedIdJson = gson.toJson(addedWithAutoIncrementedId);
		Mockito.when(mockService.addNewUser(ArgumentMatchers.any(User.class))).thenReturn(addedWithAutoIncrementedId);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/users").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseUser = result.getResponse().getContentAsString();
		
		// Assert
		
//		assertTrue(gson.fromJson(responseUser, User.class).equals(gson.fromJson(addedWithAutoIncrementedIdJson, User.class)));
		assertTrue(responseUser.equals(addedWithAutoIncrementedIdJson));
		//assertTrue(responseHeader.equals(""+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	@WithMockUser
	public void addUser_Returns_HTTP_BAD_REQUEST_And_Message_When_User_Is_Invalid() throws Exception {
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User toBeAdded = new User(0, "wrong role", "wrong email", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String toBeAddedJson = gson.toJson(toBeAdded);
		String exceptionMessage = "Invalid role email field(s) for User argument";
		Mockito.when(mockService.addNewUser(ArgumentMatchers.any(User.class))).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/users").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// Tests for deleteUserById
	@Test
	@WithMockUser
	public void deleteUserById_Returns_User_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		int id = 5;
		User dataReturned = new User(id, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String dataReturnedJson = gson.toJson(dataReturned);
		Mockito.when(mockService.removeUser(id)).thenReturn(dataReturned);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/users/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseUser = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseUser.equals(dataReturnedJson));
	}	
	
	@Test
	@WithMockUser
	public void deleteUserById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "User not found!!";
		Mockito.when(mockService.removeUser(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/users/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}

	// Tests for editUserById
	@Test
	@WithMockUser
	public void editUserById_Returns_User_And_HTTP_OK_When_Id_And_User_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		User toBeCopied = new User(1000, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User toBeSaved = new User(id, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String toBeSavedJson = gson.toJson(toBeSaved);
		Mockito.when(mockService.editUserById(ArgumentMatchers.eq(id), ArgumentMatchers.any(User.class))).thenReturn(toBeSaved);
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/users/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseUser = result.getResponse().getContentAsString();

		// Assert
		assertTrue(responseUser.equals(toBeSavedJson));
	}
	
	@Test
	@WithMockUser
	public void editUserById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		User toBeCopied = new User(1000, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "User not found!!";
		Mockito.when(mockService.editUserById(ArgumentMatchers.eq(id), ArgumentMatchers.any(User.class))).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/users/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void editUserById_Returns_HTTP_BAD_REQUEST_And_Message_When_User_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		User toBeCopied = new User(1000, "wrong role", "wrong email", "1234", "John", "1234567890", "active");
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();;
		String toBeCopiedJson = gson.toJson(toBeCopied);
		String exceptionMessage = "Invalid role email field(s) for User argument";
		Mockito.when(mockService.editUserById(ArgumentMatchers.eq(id), ArgumentMatchers.any(User.class))).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/users/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
}
