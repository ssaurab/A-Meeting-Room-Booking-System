





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    ClientRepository clientServiceDao;

    @Autowired
    public void setDao(ClientRepository dao) {
        this.clientServiceDao = dao;
    }

    @Override
    public Client addNewClient(Client toBeAdded) throws IllegalArgumentBadRequestException {
        ClientValidity uv = new ClientValidity(toBeAdded);

        if (!uv.checkValidity()) {
            throw new IllegalArgumentBadRequestException("Invalid "
                    + (!uv.emailIsValid ? "email " : "")
                    + (!uv.phoneIsValid ? "phone " : "")
                    + (!uv.zipIsValid ? "zip " : "")
                    + "field(s) for Client argument");
        } else {
            return clientServiceDao.save(toBeAdded);
        }
    }

    @Override
    public Client removeClient(int id) throws IllegalStateException, IllegalArgumentNotFoundException {
        Optional<Client> existing = clientServiceDao.findById(id);
        if (existing.isPresent()) {
            clientServiceDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("Client not found!!");
    }

    @Override
    public List<Client> findAll() {
        return (List<Client>) clientServiceDao.findAll();
    }

    @Override
    public Client findById(int id) throws IllegalArgumentNotFoundException {
        Optional<Client> clientWithId = clientServiceDao.findById(id);
        if (clientWithId.isPresent())
            return clientWithId.get();
        throw new IllegalArgumentNotFoundException("Client not found!!");
    }

    @Override
    public Client editClientById(int id, Client toBeCopied) throws IllegalArgumentBadRequestException, IllegalArgumentNotFoundException {
        ClientValidity uv = new ClientValidity(toBeCopied);
        if (!uv.checkValidity()) {
            throw new IllegalArgumentBadRequestException("Invalid "
                    + (!uv.emailIsValid ? "email " : "")
                    + (!uv.phoneIsValid ? "phone " : "")
                    + (!uv.zipIsValid ? "zip " : "")
                    + "field(s) for Client argument");
        }

        Client existing = this.findById(id);
        int existing_id = existing.getId();
        existing = toBeCopied;
        existing.setId(existing_id);
        return clientServiceDao.save(existing);
    }

    class ClientValidity {
        boolean emailIsValid, phoneIsValid, zipIsValid;

        public ClientValidity(Client client) {  // TODO check mandatory fields
            emailIsValid = (/*client.getEmail() != "" && */client.getEmail().contains("@"));   // TODO More sophisticated email checking?
            phoneIsValid = (/*client.getPhone() != "" && */(client.getPhone().length() == 10 && client.getPhone().matches("[0-9]+")));  // Check that phone number has length 10 and contains only digits
            zipIsValid = (client.getZip().length() == 6 && client.getZip().matches("[0-9]+"));
        }

        // TODO public or protected or default?
        public boolean checkValidity() {
            return emailIsValid && phoneIsValid && zipIsValid;
        }


    }

}
