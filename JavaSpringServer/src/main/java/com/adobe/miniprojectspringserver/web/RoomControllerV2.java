





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/V2/rooms")
public class RoomControllerV2 {
    RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }


    @GetMapping(value = {""})
    public ResponseEntity<?> getAllRoomsV2(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortingParam,
            @RequestParam(name = "from", required = false, defaultValue = "1") String fromString,
            @RequestParam(name = "to", required = false, defaultValue = Integer.MAX_VALUE + "") String toString,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<Room> rooms = roomService.findAll();
        int from, to;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) { // If from is not a valid integer, return from start of list
            from = 1;
        }
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) { // If to is not a valid integer, return till end of list
            to = rooms.size();
        }
        if (from > to) {
            return new ResponseEntity<>("From should be LTEQ To", HttpStatus.BAD_REQUEST);
        }
        from--;
        order = (order.equals("asc") || order.equals("des")) ? order : "asc";
        switch (sortingParam) {
            //todo: implement date
            case "capacity":
                rooms.sort(Comparator.comparingInt(Room::getCapacity));
                break;
            case "name":
                rooms.sort(Comparator.comparing(Room::getName));
                break;
            case "booking":
                rooms.sort(Comparator.comparingInt(o -> o.getBookings().size()));
                break;
            case "status":
                rooms.sort(Comparator.comparing(Room::getStatus));
                break;
            default:
                rooms.sort(Comparator.comparing(Room::getId));
                break;
        }
        if (order.equals("des"))
            Collections.reverse(rooms);

        if (from > rooms.size())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        else if (to > rooms.size())
            return new ResponseEntity<>(rooms.subList(from, rooms.size()), HttpStatus.OK);
        else
            return new ResponseEntity<>(rooms.subList(from, to), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getRoomByID(@PathVariable("id") int id) {
        Room room = roomService.findById(id);
        if (room != null) return new ResponseEntity<>(room, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = {"/{id}/layouts"})
    public Set<RoomLayout> getAllLayoutsAvailableForRoomByID(@PathVariable("id") int id) {
        Set<RoomLayout> room = roomService.getAllRoomLayoutsAvailableForThisRoom(id);
        return room;
    }

    @GetMapping(value = {"/{id}/bookings"})
    public Set<Booking> getAllBookingDoneForRoomByID(@PathVariable("id") int id) {
        return roomService.getAllBookingOfThisRoom(id);
    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addRoom(@RequestBody Room room) {
        Room newRoom = roomService.addNewRoom(room);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteRoom(@PathVariable("id") int id) {
        Room room = roomService.removeRoom(id);
        if (room != null) return new ResponseEntity<>(room, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> updateRoomById(@PathVariable("id") int id, @RequestBody Room room) {
        Room editedRoom = roomService.updateRoomById(id, room);
        if (editedRoom != null) return new ResponseEntity<>(editedRoom, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchRoomsById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        Room room = roomService.findById(id);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Room.class, (String) k);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, room, Float.parseFloat(v.toString()));
                else
                    ReflectionUtils.setField(field, room, v);
            }
        });
        Room editedRoom = roomService.updateRoomById(id, room);
        return new ResponseEntity<>(editedRoom, HttpStatus.OK);
    }


    @PutMapping(value = {"/status/{id}/{status}"})
    public ResponseEntity<?> editRoomStatusById(@PathVariable("id") int id, @PathVariable("status") String status) {
        try {
            Room toBeEditedRoom = roomService.findById(id);
            if (toBeEditedRoom == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                toBeEditedRoom.setStatus(status);
                roomService.updateRoomById(id, toBeEditedRoom);
                return new ResponseEntity<>(toBeEditedRoom, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Room not found!!")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } else { // Room argument was wrong
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }
    }


}
