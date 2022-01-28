

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {

	@Mock
	ClientRepository mockDao;
	
// Tests for Add New Client
	@Test
	public void addNewClient_Must_Return_Added_Client_When_Client_Is_Valid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client saved = new Client(10, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Mockito.when(mockDao.save(toBeAdded)).thenReturn(saved);
		
		Client toBeAdded2 = new Client(1000, "Mr.", "Mark", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client saved2 = new Client(11, "Mr.", "Mark", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Mockito.when(mockDao.save(toBeAdded2)).thenReturn(saved2);
		service.setDao(mockDao);
		
		//Act
		Client client = service.addNewClient(toBeAdded);
		Client client2 = service.addNewClient(toBeAdded2);
		//Assert
		Mockito.verify(mockDao, Mockito.times(2));
		assertTrue(client == saved);
		assertTrue(client2 == saved2);
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Email_Is_Invalid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "wrong email", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Phone_Is_Invalid_Because_Phone_Length_Is_Not_10() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "123", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Phone_Is_Invalid_Because_Phone_Contains_Non_Numeric_Characters() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "wrong phone", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Zip_Is_Invalid_Because_Zip_Length_Is_Not_6() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123", "Country");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid zip field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Zip_Is_Invalid_Because_Zip_Has_Non_Numeric_Characters() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "wrong zip", "Country");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid zip field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void addNewClient_Must_Throw_Illegal_Argument_Exeption_When_Multiple_Fields_Are_Invalid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		Client toBeAdded = new Client(1000, "Mr.", "John", "wrong email", "wrong phone", "Notes", "Company", "Address", "City", "State", "wrong zip", "Country");
		Client toBeAdded2 = new Client(1000, "Mr.", "John", "wrong email", "wrong phone", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.addNewClient(toBeAdded);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email phone zip field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
	    
	    
	    try {
	    	service.addNewClient(toBeAdded2);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email phone field(s) for Client argument")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
// Tests for Remove Client
	@Test
	public void removeClient_Must_Return_Removed_Client_When_Id_Is_Valid() {
		//Arrange
		int id=5;
		ClientServiceImpl service = new ClientServiceImpl();
		Client saved = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Optional<Client> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(id)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		Client client = service.removeClient(id);
		
		//Assert
		Mockito.verify(mockDao).deleteById(id);;
		assertTrue(client == saved);
	}
	
	@Test
	public void removeClient_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		int id=5;
		ClientServiceImpl service = new ClientServiceImpl();
		Optional<Client> returned = Optional.empty();
		Mockito.when(mockDao.findById(id)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.removeClient(id);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Client not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
// Tests for findAll
	@Test
	public void findAll_Must_Return_List_Of_Clients() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client client1 = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client client2 = new Client(1000, "Mr.", "Mark", "XYZ@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		
		List<Client> list = new ArrayList<Client>();
		list.add(client1);
		list.add(client2);
		
		Mockito.when(mockDao.findAll()).thenReturn(list);
		service.setDao(mockDao);
		
		//Act
		List<Client> returned = service.findAll();
		
		//Assert
		Mockito.verify(mockDao);
		assertTrue(returned.get(0) == client1 && returned.get(1) == client2);
	}

// Tests for findById
	@Test
	public void findById_Must_Return_Client_When_Id_Is_Valid() {
		//Arrange
		int id = 5;
		ClientServiceImpl service = new ClientServiceImpl();
		Client saved = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Optional<Client> returned = Optional.of(saved);
		Mockito.when(mockDao.findById(id)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act
		Client client = service.findById(id);
		
		//Assert
		Mockito.verify(mockDao).findById(id);;
		assertTrue(client == saved);
	}
	
	@Test
	public void findById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		int id=5;
		ClientServiceImpl service = new ClientServiceImpl();
		Optional<Client> returned = Optional.empty();
		Mockito.when(mockDao.findById(id)).thenReturn(returned);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.findById(id);
	    	fail(); // Should not reach here
	    }
	    catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Client not found!!")) {
	    		fail();
	    	}
	    }
	    catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}

// Tests for editById
	@Test
	public void editClientById_Must_Return_Edited_Client_When_Id_Is_Valid() {
		//Arrange
		int id=5;
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mrs.", "Jen", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client saved = new Client(id, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client toBeSaved = new Client(id, "Mrs.", "Jen", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		
		Optional<Client> found = Optional.of(saved);
		Mockito.when(mockDao.findById(id)).thenReturn(found);
		Mockito.when(mockDao.save(toBeSaved)).thenReturn(toBeSaved);
		service.setDao(mockDao);
		
		//Act
		Client returned = service.editClientById(id, toBeCopied);
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao).save(any(Client.class));
		System.out.println(returned.equals(toBeSaved));
		assertTrue(returned.equals(toBeSaved));
	}
	
	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Email_Is_Invalid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mr.", "John", "wrong email", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Phone_Is_Invalid_Because_Phone_Length_Is_Not_10() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mr.", "John", "123@abc.com", "123", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}

	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Phone_Is_Invalid_Because_Phone_Contains_Non_Numeric_Characters() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mr.", "John", "123@abc.com", "wrong phone", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid phone field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}

	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Zip_Is_Invalid_Because_Zip_Length_Is_Not_6() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123", "Country");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid zip field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}
	
	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Zip_Is_Invalid_Because_Zip_Has_Non_Numeric_Characters() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "wrong zip", "Country");
		service.setDao(mockDao);

		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid zip field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}

	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exeption_When_Multiple_Fields_Are_Invalid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied1 = new Client(1000, "Mr.", "John", "wrong email", "wrong phone", "Notes", "Company", "Address", "City", "State", "wrong zip", "Country");
		Client toBeCopied2 = new Client(1000, "Mr.", "John", "wrong email", "wrong phone", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.editClientById(5, toBeCopied1);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email phone zip field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
	    
	    
	    try {
	    	service.addNewClient(toBeCopied2);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Invalid email phone field(s) for Client argument")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }

		//Assert
		Mockito.verify(mockDao, Mockito.never());
	}

	@Test
	public void editClientById_Must_Throw_Illegal_Argument_Exception_When_Id_Is_Invalid() {
		//Arrange
		ClientServiceImpl service = new ClientServiceImpl();
		
		Client toBeCopied = new Client(1000, "Mrs.", "Jen", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		Client saved = new Client(1, "Mr.", "John", "123@abc.com", "1234567890", "Notes", "Company", "Address", "City", "State", "123456", "Country");
		
		Optional<Client> found = Optional.empty();
		Mockito.when(mockDao.findById(saved.getId())).thenReturn(found);
		service.setDao(mockDao);
		
		//Act and Assert
	    try {
	    	service.editClientById(1, toBeCopied);
	    	fail(); // Should not reach here
	    } catch (IllegalArgumentException e) {
	    	if(!e.getMessage().equals("Client not found!!")) {
	    		fail();
	    	}
	    } catch (Throwable e) {  // Not expected exception class
	    	fail();
	    }
		
		//Assert
		Mockito.verify(mockDao).findById(saved.getId());
		Mockito.verify(mockDao, Mockito.never()).save(any(Client.class));
	}
	
}

