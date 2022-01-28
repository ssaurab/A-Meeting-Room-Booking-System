




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
@RequestMapping("/roomlayouts")
public class RoomLayoutController {
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
        Set<Room> rooms = roomLayoutService.getAllRoomAvailableForThisRoomLayout(id);
        return rooms;
    }
}
