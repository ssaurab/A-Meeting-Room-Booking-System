






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoomLayoutServiceImpl implements RoomLayoutService {
    private RoomLayoutRepository roomLayoutServiceDao;

    @Autowired
    public void setRoomLayoutServiceDao(RoomLayoutRepository roomLayoutServiceDao) {
        this.roomLayoutServiceDao = roomLayoutServiceDao;
    }

    public String validateRoomLayout(RoomLayout toBeAdded) {
        if (toBeAdded.getTitle().equals(""))
            return "Layout name can't be empty";
        else
            return "valid";
    }

    @Override
    public RoomLayout addNewRoomLayout(RoomLayout toBeAdded) throws IllegalArgumentBadRequestException {
        String validationMsg = validateRoomLayout(toBeAdded);
        if (validationMsg.equals("valid")) {
            RoomLayout added = roomLayoutServiceDao.save(toBeAdded);
            return added;
        } else {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }
    }

    @Override
    public RoomLayout removeRoomLayout(int id) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        Optional<RoomLayout> existing = roomLayoutServiceDao.findById(id);
        if (existing.isPresent()) {
            Set<Room> rooms = existing.get().getRooms();
            if (!rooms.isEmpty()) {
                throw new IllegalArgumentBadRequestException("Cannot delete a RoomLayout that is required by a Room in database");
            }
//        	for(Room room : rooms) {
//        		Set<RoomLayout> layouts = room.getLayouts();
//        		if(layouts.size() == 1) { // This room supports only this room layout
//        			throw new IllegalArgumentBadRequestException("Cannot remove RoomLayout: It is the only RoomLayout that Room with name `"+room.getName()+"` supports");
//        		}
//        	}
            roomLayoutServiceDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("RoomLayout not found!!");
    }

    @Override
    public List<RoomLayout> findAll() {
        return (List<RoomLayout>) roomLayoutServiceDao.findAll();
    }

    @Override
    public Set<Room> getAllRoomAvailableForThisRoomLayout(int id) throws IllegalArgumentNotFoundException {
        Optional<RoomLayout> roomLayoutWithId = roomLayoutServiceDao.findById(id);
        if (roomLayoutWithId.isPresent()) {
            return roomLayoutWithId.get().getRooms();
        }
        throw new IllegalArgumentNotFoundException("RoomLayout not found!!");
    }

    @Override
    public RoomLayout findById(int id) throws IllegalArgumentNotFoundException {
        Optional<RoomLayout> roomWithId = roomLayoutServiceDao.findById(id);
        if (roomWithId.isPresent())
            return roomWithId.get();
        throw new IllegalArgumentNotFoundException("RoomLayout not found!!");
    }

    @Override
    public RoomLayout updateRoomLayoutById(int id, RoomLayout toBeCopied) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        String validationMsg = validateRoomLayout(toBeCopied);
        if (!validationMsg.equals("valid")) {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }

        Optional<RoomLayout> roomWithId = roomLayoutServiceDao.findById(id);
        if (roomWithId.isPresent()) {
            RoomLayout existing = roomWithId.get();
            int existing_id = existing.getId();
            existing = toBeCopied;
            existing.setId(existing_id);
            return roomLayoutServiceDao.save(existing);
        }
        throw new IllegalArgumentNotFoundException("RoomLayout not found!!");
    }
}
