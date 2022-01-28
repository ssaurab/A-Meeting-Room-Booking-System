





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/V1/rooms")
public class RoomControllerV1 {
    RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getRoomByID(@PathVariable("id") int id) {
        Room room = roomService.findById(id);
        if (room != null) return new ResponseEntity<>(room, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = {"/{id}/layouts"})
    public Set<RoomLayout> getAllLayoutsAvailableForRoomByID(@PathVariable("id") int id) {
        Set<RoomLayout> roomLayouts = roomService.getAllRoomLayoutsAvailableForThisRoom(id);
        return roomLayouts;
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
