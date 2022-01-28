

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@RunWith(SpringRunner.class)
@ContextConfiguration
@WebMvcTest({UserControllerV1.class})
public class UserControllerV1Test {
	
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
	
	// Tests for getAllUsersV1
	@Test
	@WithMockUser
	public void getAllUsersV1_Returns_List_Of_Users() throws Exception{
		//Arrange
		User admin = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user1 = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		User user2 = new User(2, "editor", "123@abc.com", "1234", "John", "1234567890", "inactive");
		List<User> savedList = new ArrayList<User>();
		savedList.add(user1);
		savedList.add(user2);
		
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String savedListJson = gson.toJson(savedList);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		Mockito.when(mockService.isLogged()).thenReturn(admin);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/users").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(savedListJson, content);
	}	

}
