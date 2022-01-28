





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rooms")
public class RoomController {
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

}
