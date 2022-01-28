



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/V2/clients")
public class ClientControllerV2 {
    ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    // No Sorting needed for Clients as clients are not displayed on any page
    @GetMapping(value = {""})
    public ResponseEntity<List<Client>> getAllClients() {
        return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getUserByID(@PathVariable("id") int id) {
        Client client = clientService.findById(id);
        if (client != null) return new ResponseEntity<>(client, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addClient(@RequestBody Client client) {
        Client newClient = clientService.addNewClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        Client client = clientService.removeClient(id);
        if (client != null) return new ResponseEntity<>(client, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> editClientById(@PathVariable("id") int id, @RequestBody Client client) {
        Client toBeEdited = clientService.editClientById(id, client);
        if (toBeEdited != null) return new ResponseEntity<>(toBeEdited, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchClientById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        Client client = clientService.findById(id);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Client.class, (String) k);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, client, Float.parseFloat(v.toString()));
                else
                    ReflectionUtils.setField(field, client, v);
            }
        });
        Client edited = clientService.editClientById(id, client);
        return new ResponseEntity<>(edited, HttpStatus.OK);
    }

}
