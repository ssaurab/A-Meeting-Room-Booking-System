



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/V2/equipments")
public class EquipmentsControllerV2 {

    EquipmentService equipmentService;

    @Autowired
    public void setEquipmentsService(EquipmentService EquipmentService) {
        this.equipmentService = EquipmentService;
    }


    @GetMapping(value = {""})
    public ResponseEntity<?> getAllEquipmentsV2(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortingParam,
            @RequestParam(name = "from", required = false, defaultValue = "1") String fromString,
            @RequestParam(name = "to", required = false, defaultValue = Integer.MAX_VALUE + "") String toString,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<Equipment> equipments = equipmentService.findAll();
        int from, to;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) { // If from is not a valid integer, return from start of list
            from = 1;
        }
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) { // If to is not a valid integer, return till end of list
            to = equipments.size();
        }
        if (from > to) {
            return new ResponseEntity<>("From should be LTEQ To", HttpStatus.BAD_REQUEST);
        }
        from--;
        order = (order.equals("asc") || order.equals("des")) ? order : "asc";
        switch (sortingParam) {
            case "title":
                equipments.sort(Comparator.comparing(Equipment::getTitle));
                break;
            case "price":
                equipments.sort(Comparator.comparing(Equipment::getPrice));
                break;
            case "bookmultipleunit":
                equipments.sort(Comparator.comparing(Equipment::isBookMultipleUnit));
                break;
            default:
                equipments.sort(Comparator.comparing(Equipment::getId));
                break;
        }
        if (order.equals("des"))
            Collections.reverse(equipments);
        if (from > equipments.size())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        else if (to > equipments.size())
            return new ResponseEntity<>(equipments.subList(from, equipments.size()), HttpStatus.OK);
        else
            return new ResponseEntity<>(equipments.subList(from, to), HttpStatus.OK);
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

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchEquipmentsById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        Equipment equipment = equipmentService.findById(id);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Equipment.class, (String) k);
            if (field != null) {
                String type = field.getType().getName();
                field.setAccessible(true);
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, equipment, Float.parseFloat(v.toString()));
                else
                    ReflectionUtils.setField(field, equipment, v);
            }
        });
        Equipment editedEquipment = equipmentService.updateById(equipment, id);
        return new ResponseEntity<>(editedEquipment, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteEquipments(@PathVariable("id") int id) {
        Equipment equipment = equipmentService.removeEquipment(id);
        if (equipment != null) return new ResponseEntity<>(equipment, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
