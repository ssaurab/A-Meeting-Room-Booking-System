



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentsController {

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


}
