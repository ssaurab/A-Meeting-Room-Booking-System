



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/V1/equipments")
public class EquipmentsControllerV1 {

    EquipmentService equipmentService;

    @Autowired
    public void setEquipmentsService(EquipmentService EquipmentService) {
        this.equipmentService = EquipmentService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        return new ResponseEntity<>(equipmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getEquipmentsByID(@PathVariable("id") int id) {
        Equipment equipment = equipmentService.findById(id);
        if (equipment != null) return new ResponseEntity<>(equipment, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addEquipments(@RequestBody Equipment Equipment) {
        Equipment newEquipment = equipmentService.addNewEquipment(Equipment);
        return new ResponseEntity<>(newEquipment, HttpStatus.CREATED);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> updateEquipments(@RequestBody Equipment Equipment, @PathVariable("id") int id) {
        Equipment toBeUpdatedEquipment = equipmentService.updateById(Equipment, id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/Equipments/" + toBeUpdatedEquipment.getId()));
        return new ResponseEntity<>(toBeUpdatedEquipment, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteEquipments(@PathVariable("id") int id) {
        Equipment equipment = equipmentService.removeEquipment(id);
        if (equipment != null) return new ResponseEntity<>(equipment, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
