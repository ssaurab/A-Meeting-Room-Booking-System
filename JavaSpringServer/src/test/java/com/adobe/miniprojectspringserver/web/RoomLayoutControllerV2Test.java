

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;





import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(SpringRunner.class)
@WebMvcTest({RoomLayoutControllerV2.class})
@AutoConfigureMockMvc(addFilters = false) 
public class RoomLayoutControllerV2Test {

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
	

	
// Tests for getAllRoomLayoutsV2
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_Entire_List_Of_RoomLayouts_In_Ascending_Order_Of_Id_By_Default() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		returned.add(roomLayout1);
		returned.add(roomLayout2);
		returned.add(roomLayout3);
		returned.add(roomLayout4);
		returned.add(roomLayout5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);

		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_Correct_Range_Of_List_Of_RoomLayouts_In_Descending_Order_Of_Title() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		returned.add(roomLayout2);
		returned.add(roomLayout3);
		returned.add(roomLayout4);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
				.param("sort", "title").param("from", "2").param("to", "4").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_Correct_Range_Of_List_Of_RoomLayouts_In_Descending_Order_Of_Title_When_To_Parameter_GT_Number_Of_RoomLayouts() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		returned.add(roomLayout3);
		returned.add(roomLayout4);
		returned.add(roomLayout5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
				.param("sort", "title").param("from", "3").param("to", "400").param("order", "des")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
	}
	
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_Empty_List_Of_RoomLayouts_When_From_Parameter_GT_Number_Of_RoomLayouts() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
				.param("from", "300").param("to", "400")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(204))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
	}
//	
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_HTTP_BAD_REQUEST_When_From_GT_To() throws Exception{
		//Arrange
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
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
	public void getAllRoomLayoutsV2_Returns_List_Of_RoomLayouts_From_Beginning_When_From_Is_Not_An_Integer() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		returned.add(roomLayout1);
		returned.add(roomLayout2);
		returned.add(roomLayout3);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
				.param("sort", "id").param("from", "NAN").param("to", "3")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
	}	
	
	@Test
	@WithMockUser
	public void getAllRoomLayoutsV2_Returns_List_Of_RoomLayouts_Till_End_When_To_Is_Not_An_Integer() throws Exception{
		//Arrange
		RoomLayout roomLayout1 = new RoomLayout(1,"TitleE","image".getBytes());
		RoomLayout roomLayout2 = new RoomLayout(2,"TitleD","image".getBytes());
		RoomLayout roomLayout3 = new RoomLayout(3,"TitleC","image".getBytes());
		RoomLayout roomLayout4 = new RoomLayout(4,"TitleB","image".getBytes());
		RoomLayout roomLayout5 = new RoomLayout(5,"TitleA","image".getBytes());
		List<RoomLayout> savedList = new ArrayList<RoomLayout>();
		savedList.add(roomLayout1);
		savedList.add(roomLayout2);
		savedList.add(roomLayout3);
		savedList.add(roomLayout4);
		savedList.add(roomLayout5);
		
		List<RoomLayout> returned = new ArrayList<RoomLayout>();
		returned.add(roomLayout2);
		returned.add(roomLayout3);
		returned.add(roomLayout4);
		returned.add(roomLayout5);
		 
		Mockito.when(mockService.findAll()).thenReturn(savedList);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts")
				.param("sort", "id").param("from", "2").param("to", "NAN")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String content = result.getResponse().getContentAsString();
		
		// Assert
		List<RoomLayout> roomLayout = new ObjectMapper().readValue(content, (new ObjectMapper()).getTypeFactory().constructCollectionType(List.class, RoomLayout.class));
		assertTrue(roomLayout.equals(returned));
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/V2/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
			
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));;
	}

	// Tests for addRoomLayout
	class JsonIgnoreAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> clazz) {
	        return clazz.getAnnotation(JsonIgnore.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(JsonIgnore.class) != null;
	    }
	}
	
	@Test
	@WithMockUser
	public void addRoomLayout_Returns_Added_RoomLayout_And_Header_And_HTTP_OK_When_RoomLayout_Is_Valid() throws Exception{
		
		//Arrange
		RoomLayout toBeAdded = new RoomLayout(1000,"Panorama","".getBytes());
		RoomLayout addedWithAutoIncrementedId = new RoomLayout(5,"Panorama","".getBytes());
//		Set<RoomLayoutLayout> layouts = new HashSet<RoomLayoutLayout>(); 
//		layouts.add(new RoomLayoutLayout(1, "title", "image".getBytes()));
//		toBeAdded.setLayouts(layouts);
//		addedWithAutoIncrementedId.setLayouts(layouts);
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String toBeAddedJson = gson.toJson(toBeAdded).replace("[]", "\"\"");;
		Mockito.when(mockService.addNewRoomLayout(toBeAdded)).thenReturn(addedWithAutoIncrementedId);
		
		//Act
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/roomlayouts").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
			.andExpect(MockMvcResultMatchers.status().is(201))
			.andReturn();
		String responseRoomLayout = result.getResponse().getContentAsString();
		
		// Assert
		Mockito.verify(mockService).addNewRoomLayout(ArgumentMatchers.eq(toBeAdded));
		RoomLayout roomLayout = new ObjectMapper().readValue(responseRoomLayout, RoomLayout.class);
		assertTrue(roomLayout.equals(addedWithAutoIncrementedId));
		//assertTrue(responseHeader.equals(""+addedWithAutoIncrementedId.getId()));  // todo: look into it
	}
	
	@Test
	@WithMockUser
	public void addRoomLayout_Returns_HTTP_BAD_REQUEST_And_Message_When_RoomLayout_Is_Invalid() throws Exception {
		//Arrange
		RoomLayout toBeAdded = new RoomLayout(1000,"","".getBytes());
//		Set<RoomLayoutLayout> layouts = new HashSet<RoomLayoutLayout>(); 
//		layouts.add(new RoomLayoutLayout(1, "title", "image".getBytes()));
//		toBeAdded.setLayouts(layouts);
		Gson gson = new Gson();
		String toBeAddedJson = gson.toJson(toBeAdded).replace("[]", "\"\"");
		String exceptionMessage = "Name of roomLayout can't be empty";
		Mockito.when(mockService.addNewRoomLayout(toBeAdded)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/V2/roomlayouts").contentType(MediaType.APPLICATION_JSON).content(toBeAddedJson))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	
	// Tests for deleteRoomLayoutById
	@Test
	@WithMockUser
	public void deleteRoomLayoutById_Returns_RoomLayout_And_HTTP_NO_CONTENT_When_Id_Is_Valid() throws Exception{
		//Arrange
		int id = 5;
		RoomLayout dataReturned = new RoomLayout(1,"Panorama","image".getBytes());
		Mockito.when(mockService.removeRoomLayout(id)).thenReturn(dataReturned);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is(204))
			.andReturn();
		String responseRoomLayout = result.getResponse().getContentAsString();

		// Assert
		RoomLayout roomLayout = new ObjectMapper().readValue(responseRoomLayout, RoomLayout.class);
		assertTrue(roomLayout.equals(dataReturned));
	}	
	
	@Test
	@WithMockUser
	public void deleteRoomLayoutById_Returns_HTTP_NOT_FOUND_With_Message_When_Id_Is_Invalid() throws Exception {
		//Arrange
		int id = 5;
		String exceptionMessage = "RoomLayout not found!!";
		Mockito.when(mockService.removeRoomLayout(id)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/V2/roomlayouts/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	// Tests for updateRoomLayoutById
	@Test
	@WithMockUser
	public void updateRoomLayoutById_Returns_RoomLayout_And_HTTP_OK_When_Id_And_RoomLayout_Are_Valid() throws Exception{
		//Arrange
		int id = 5;
		RoomLayout toBeCopied = new RoomLayout(1000,"New Panorama","".getBytes());
		RoomLayout toBeSaved = new RoomLayout(id,"Panorama","".getBytes());
		Gson gson = new GsonBuilder().setExclusionStrategies(new JsonIgnoreAnnotationExclusionStrategy() ).serializeNulls().create();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");
		Mockito.when(mockService.updateRoomLayoutById(id, toBeCopied)).thenReturn(toBeSaved);
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/roomlayouts/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andReturn();
		String responseRoomLayout = result.getResponse().getContentAsString();

		// Assert
		RoomLayout roomLayout = new ObjectMapper().readValue(responseRoomLayout, RoomLayout.class);
		assertTrue(roomLayout.equals(toBeSaved));
	}
	
	@Test
	@WithMockUser
	public void updateRoomLayoutById_Returns_HTTP_NOT_FOUND_And_Message_When_Id_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		RoomLayout toBeCopied = new RoomLayout(1000,"New Panorama","".getBytes());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");
		String exceptionMessage = "RoomLayout not found!!";
		Mockito.when(mockService.updateRoomLayoutById(id, toBeCopied)).thenThrow(new IllegalArgumentNotFoundException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/roomlayouts/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(404))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
	
	@Test
	@WithMockUser
	public void updateRoomLayoutById_Returns_HTTP_BAD_REQUEST_And_Message_When_RoomLayout_Is_Invalid() throws Exception{
		//Arrange
		int id = 5;
		RoomLayout toBeCopied = new RoomLayout(1000,"","".getBytes());
		Gson gson = new Gson();
		String toBeCopiedJson = gson.toJson(toBeCopied).replace("[]", "\"\"");
		String exceptionMessage = "Layout name can't be empty";
		Mockito.when(mockService.updateRoomLayoutById(id, toBeCopied)).thenThrow(new IllegalArgumentBadRequestException(exceptionMessage));
		
		//Act and Assert
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/V2/roomlayouts/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toBeCopiedJson))
			.andExpect(MockMvcResultMatchers.status().is(400))
			.andReturn();
		String response = result.getResponse().getContentAsString();

		// Assert
		assertTrue(response.contains(exceptionMessage));
	}
}
