







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomServiceDao;

    @Autowired
    public void setRoomServiceDao(RoomRepository roomServiceDao) {
        this.roomServiceDao = roomServiceDao;
    }

    public String validateRoomData(Room toBeAdded) {
        if (toBeAdded.getName().length() == 0)
            return "Name of room can't be empty";
        else if (!(new ArrayList<>(Arrays.asList("active", "inactive"))).contains(toBeAdded.getStatus()))
            return "Invalid Argument for status";
        else if (toBeAdded.getCapacity() < 1)
            return "Capacity can't be less than 1";
        else if (toBeAdded.getPricePerDay() < 0)
            return "Invalid value for Price Per Day.";
        else if (toBeAdded.getPricePerHour() < 0)
            return "Invalid value for Price Per Hour.";
        else if (toBeAdded.getPricePerHalfDay() < 0)
            return "Invalid value for Price Per Half Hour.";
        else
            return "valid";

    }

    @Override
    public Room addNewRoom(Room toBeAdded) throws IllegalArgumentBadRequestException {
        String validationMsg = validateRoomData(toBeAdded);
        if (validationMsg.equals("valid")) {
            Room added = roomServiceDao.save(toBeAdded);
            return added;
        } else {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }
    }

    @Override
    public Room removeRoom(int id) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        Optional<Room> existing = roomServiceDao.findById(id);
        if (existing.isPresent()) {
            Set<Booking> bookings = existing.get().getBookings();
            if (!bookings.isEmpty()) {
                throw new IllegalArgumentBadRequestException("Cannot delete a Room that has a Booking.");
            }

            roomServiceDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("Room not found!!");
    }

    @Override
    public List<Room> findAll() {
        return (List<Room>) roomServiceDao.findAll();
    }

    @Override
    public Room findById(int id) throws IllegalArgumentNotFoundException {
        Optional<Room> roomWithId = roomServiceDao.findById(id);
        if (roomWithId.isPresent())
            return roomWithId.get();
        throw new IllegalArgumentNotFoundException("Room not found!!");
    }

    @Override
    public Set<RoomLayout> getAllRoomLayoutsAvailableForThisRoom(int id) throws IllegalArgumentNotFoundException {
        Optional<Room> roomWithId = roomServiceDao.findById(id);
        if (roomWithId.isPresent()) {
            System.out.println(roomWithId.get());
            return roomWithId.get().getLayouts();
        }
        throw new IllegalArgumentNotFoundException("Room not found!!");
    }

    @Override
    public Set<Booking> getAllBookingOfThisRoom(int id) throws IllegalArgumentNotFoundException {
        Optional<Room> roomWithId = roomServiceDao.findById(id);
        if (roomWithId.isPresent()) {
            return roomWithId.get().getBookings();
        }
        throw new IllegalArgumentNotFoundException("Room not found!!");
    }

    @Override
    public Room updateRoomById(int id, Room toBeCopied) throws IllegalArgumentBadRequestException, IllegalArgumentNotFoundException {
        String validationMsg = validateRoomData(toBeCopied);
        if (!validationMsg.equals("valid")) {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }

        Optional<Room> roomWithId = roomServiceDao.findById(id);
        if (roomWithId.isPresent()) {
            Room existing = roomWithId.get();
            int existing_id = existing.getId();
            existing = toBeCopied;
            existing.setId(existing_id);
            return roomServiceDao.save(existing);
        }
        throw new IllegalArgumentNotFoundException("Room not found!!");
    }


}
