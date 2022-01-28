




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/V2/roomlayouts")
public class RoomLayoutControllerV2 {
    RoomLayoutService roomLayoutService;

    @Autowired
    public void setRoomService(RoomLayoutService roomLayoutService) {
        this.roomLayoutService = roomLayoutService;
    }


    @GetMapping(value = {""})
    public ResponseEntity<?> getAllRoomLayoutsV2(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortingParam,
            @RequestParam(name = "from", required = false, defaultValue = "1") String fromString,
            @RequestParam(name = "to", required = false, defaultValue = Integer.MAX_VALUE + "") String toString,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<RoomLayout> roomLayouts = roomLayoutService.findAll();
        int from, to;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) { // If from is not a valid integer, return from start of list
            from = 1;
        }
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) { // If to is not a valid integer, return till end of list
            to = roomLayouts.size();
        }
        if (from > to) {
            return new ResponseEntity<>("From should be LTEQ To", HttpStatus.BAD_REQUEST);
        }
        from--;
        order = (order.equals("asc") || order.equals("des")) ? order : "asc";
        switch (sortingParam) {
            case "title":
                roomLayouts.sort(Comparator.comparing(RoomLayout::getTitle));
                break;
            default:
                roomLayouts.sort(Comparator.comparing(RoomLayout::getId));
                break;
        }
        if (order.equals("des"))
            Collections.reverse(roomLayouts);

        if (from > roomLayouts.size())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        else if (to > roomLayouts.size())
            return new ResponseEntity<>(roomLayouts.subList(from, roomLayouts.size()), HttpStatus.OK);
        else
            return new ResponseEntity<>(roomLayouts.subList(from, to), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getRoomLayoutByID(@PathVariable("id") int id) {
        RoomLayout editedLayout = roomLayoutService.findById(id);
        if (editedLayout != null) return new ResponseEntity<>(editedLayout, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = {"/{id}/rooms"})
    public Set<Room> getAllRoomsAvailableForRoomLayoutByID(@PathVariable("id") int id) {
        Set<Room> rooms = roomLayoutService.getAllRoomAvailableForThisRoomLayout(id);
        return rooms;
    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addRoomLayout(@RequestBody RoomLayout layout) {
        RoomLayout roomLayout = roomLayoutService.addNewRoomLayout(layout);
        return new ResponseEntity<>(roomLayout, HttpStatus.CREATED);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteRoomLayout(@PathVariable("id") int id) {
        RoomLayout roomLayout = roomLayoutService.removeRoomLayout(id);
        if (roomLayout != null) return new ResponseEntity<>(roomLayout, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> updateRoomLayoutById(@PathVariable("id") int id, @RequestBody RoomLayout booking) {
        RoomLayout editedLayout = roomLayoutService.updateRoomLayoutById(id, booking);
        if (editedLayout != null) return new ResponseEntity<>(editedLayout, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchRoomLayoutsById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        RoomLayout roomLayout = roomLayoutService.findById(id);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(RoomLayout.class, (String) k);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, roomLayout, Float.parseFloat(v.toString()));
                else
                    ReflectionUtils.setField(field, roomLayout, v);
            }
        });
        RoomLayout edited = roomLayoutService.updateRoomLayoutById(id, roomLayout);
        return new ResponseEntity<>(edited, HttpStatus.OK);
    }

}
