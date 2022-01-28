




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/V1/roomlayouts")
public class RoomLayoutControllerV1 {
    RoomLayoutService roomLayoutService;

    @Autowired
    public void setRoomService(RoomLayoutService roomLayoutService) {
        this.roomLayoutService = roomLayoutService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<List<RoomLayout>> getAllRoomLayouts() {
        return new ResponseEntity<>(roomLayoutService.findAll(), HttpStatus.OK);
    }


    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getRoomLayoutByID(@PathVariable("id") int id) {
        RoomLayout roomLayout = roomLayoutService.findById(id);
        if (roomLayout != null) return new ResponseEntity<>(roomLayout, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = {"/{id}/rooms"})
    public Set<Room> getAllRoomsAvailableForRoomLayoutByID(@PathVariable("id") int id) {
        return roomLayoutService.getAllRoomAvailableForThisRoomLayout(id);
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
}
