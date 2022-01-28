



import java.util.List;

public interface ClientService {
    Client addNewClient(Client toBeAdded);

    Client removeClient(int id);

    List<Client> findAll();

    Client findById(int id);

    Client editClientById(int id, Client toBeCopied);

}
