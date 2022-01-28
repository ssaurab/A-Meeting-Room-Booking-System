



import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest({SnackControllerV1.class})
@AutoConfigureMockMvc(addFilters = false) 
class SnackControllerV1Test {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
    SnackService mockService;
	
	// tests for getAllSnacks
	@Test
	@WithMockUser
	public void getAllSnacks_Returns_List_Of_Snacks_And_HTTP_OK() throws Exception{
		//Arrange
		Snack snack1 = new Snack(1, "title", (float)1000.0);
		Snack snack2 = new Snack(2, "title 2", (float)1000.0);
		List<Snack> savedList = new ArrayList<Snack>();
		savedList.add(snack1);
		savedList.add(snack2);
		
		Gson gson = new Gson();
		String savedListJson = gson.toJson(savedList);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V1/snacks").accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		assertEquals(savedListJson, content);
	}

}
