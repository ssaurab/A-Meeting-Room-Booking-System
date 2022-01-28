

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@Mock
	UserRepository mockDao;

// Tests for loadUserByUsername
	@Test
	public void loadUserByUsername_Must_Return_UserDetails_When_Username_Is_Present_In_Database() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User saved = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockDao.findByName("John")).thenReturn(saved);
		
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setUser(saved);
		service.setDao(mockDao);
		
		//Act
		UserDetails userDetailsReturned = service.loadUserByUsername("John");
		//Assert
		assertEquals(1, Mockito.mockingDetails(mockDao).getInvocations().size());
		assertTrue((userDetailsReturned).equals((UserDetails)userDetails));
	}
	
	@Test
	public void loadUserByUsername_Throws_UsernameNotFoundException_When_Username_Is_Absent_In_Database() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		Mockito.when(mockDao.findByName("John")).thenReturn(null);
		
		service.setDao(mockDao);
		
		//Act
		try {
			service.loadUserByUsername("John");
	    	fail(); // Should not reach here
	    }
	    catch (UsernameNotFoundException e) {
	    	if(!e.getMessage().equals("User does not exist with the name John")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		//Assert
		assertEquals(1, Mockito.mockingDetails(mockDao).getInvocations().size());
	}
	
// Tests for Add New User
	@Test
	public void addNewUser_Must_Return_Added_User_When_User_Is_Valid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeAdded = new User(0, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		User saved = new User(1, "administrator", "123@abc.com", "1234", "John", "1234567890", "active");
		Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
		
		User toBeAdded2 = new User(0, "editor", "123@abc.com", "Abc12345", "John", "1234567890", "inactive");
		User saved2 = new User(2, "editor", "123@abc.com", "1234", "John", "1234567890", "inactive");
		Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
		service.setDao(mockDao);
		
		//Act
		User user = service.addNewUser(toBeAdded);
		User user2 = service.addNewUser(toBeAdded2);
		//Assert
		assertEquals(2, Mockito.mockingDetails(mockDao).getInvocations().size());
//		Mockito.verify(mockDao, Mockito.times(2));
		assertSame(user, saved);
		assertSame(user2, saved2);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Role_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "any wrong role", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid role field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Email_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "administrator", "wrong email", "Abc12345", "John", "1234567890", "active");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Phone_Is_Invalid_Because_Phone_Length_Is_Not_10() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "administrator", "123@abc.com", "Abc12345", "John", "123", "active");
		service.setDao(mockDao);
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Phone_Is_Invalid_Because_Phone_Contains_Non_Numeric_Characters() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "administrator", "123@abc.com", "Abc12345", "John", "phone number should not contain non-numeric characters", "active");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Status_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "wrong status");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid status field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void addNewUser_Must_Throw_Illegal_Argument_Exeption_When_Multiple_Fields_Are_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User toBeAdded = new User(0, "wrong role", "wrong email", "Abc12345", "John", "wrong phone", "wrong status");
		User toBeAdded2 = new User(0, "editor", "wrong email", "Abc12345", "John", "1234567890", "wrong status");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewUser(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid role email phone status field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
	    
	    
	    try {
	    	service.addNewUser(toBeAdded2);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email status field(s) for User argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
// Tests for Remove User
	@Test
	public void removeUser_Must_Return_Removed_User_When_Id_Is_Valid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User saved = new User(1, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		Optional<User> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		User user = service.removeUser(1);
		
		//Assert
		Mockito.verify(mockDao).deleteById(1);;
		assertSame(user, saved);
	}
	
	@Test
	public void removeUser_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		Optional<User> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeUser(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("User not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);	
	}
	
// Tests for findAll
	@Test
	public void findAll_Must_Return_List_Of_Users() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User user1 = new User(0, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		User user2 = new User(0, "editor", "123@abc.com", "Abc12345", "John", "1234567890", "inactive");
		
		List<User> list = new ArrayList<User>();
		list.add(user1);
		list.add(user2);
		
		Mockito.when(mockDao.findAll()).thenReturn(list);
		service.setDao(mockDao);
		
		//Act
		List<User> returned = service.findAll();
		
		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
		assertTrue(returned.get(0) == user1 && returned.get(1) == user2);
	}

// Tests for findById
	@Test
	public void findById_Must_Return_User_When_Id_Is_Valid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		User saved = new User(1, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		Optional<User> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		User user = service.findById(1);
		
		//Assert
		Mockito.verify(mockDao).findById(1);;
		assertSame(user, saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		Optional<User> returned = Optional.empty();
		Mockito.when(mockDao.findById(1)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(1);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("User not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		assertTrue(Mockito.mockingDetails(mockDao).getInvocations().size()>0);
	}

// Tests for editById
	@Test
	public void editUserById_Must_Return_Edited_User_When_Id_Is_Valid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "editor", "123@abc.com", "Abc12345", "Mark", "1234567890", "inactive");
		User saved = new User(5, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		User toBeSaved = new  User(5, "editor", "123@abc.com", "Abc12345", "Mark", "1234567890", "inactive");
		
		Optional<User> found = Optional.of(saved);
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setDao(mockDao);
		
		//Act
		User returned = service.editUserById(5, toBeCopied);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(User.class));
		System.out.println(returned);
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Role_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "wrong role", "123@abc.com", "Abc12345", "Mark", "1234567890", "inactive");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid role field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Email_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "administrator", "wrong email", "Abc12345", "Mark", "1234567890", "inactive");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}
	
	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Phone_Is_Invalid_Because_Phone_Length_Is_Not_10() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "administrator", "123@abc.com", "Abc12345", "Mark", "123", "inactive");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Phone_Is_Invalid_Because_Phone_Contains_Non_Numeric_Characters() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "administrator", "123@abc.com", "Abc12345", "Mark", "should not contain non-numeric characters", "inactive");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Status_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "administrator", "123@abc.com", "Abc12345", "Mark", "1234567890", "wrong status");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid status field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exeption_When_Multiple_Fields_Are_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied1 = new User(10, "wrong role", "wrong email", "Abc1234545", "Mark", "wrong phone", "wrong status");
		User toBeCopied2 = new User(10, "wrong role", "123@abc.com", "Abc1234545", "Mark", "wrong phone", "active");
		
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied1);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid role email phone status field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
	    
	    
	    try {
	    	service.addNewUser(toBeCopied2);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid role phone field(s) for User argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verifyNoInteractions(mockDao);
	}

	@Test
	public void editUserById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		UserServiceImpl service = new UserServiceImpl();
		
		User toBeCopied = new User(10, "editor", "123@abc.com", "Abc12345", "Mark", "1234567890", "inactive");
		User saved = new User(5, "administrator", "123@abc.com", "Abc12345", "John", "1234567890", "active");
		
		Optional<User> found = Optional.empty();
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.editUserById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("User not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao, Mockito.never()).save(any(User.class));
	}
	
}
