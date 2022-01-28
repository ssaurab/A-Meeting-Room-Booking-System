





import java.util.List;
import java.util.Set;

public interface RoomService {
    Room addNewRoom(Room toBeAdded);

    Room removeRoom(int id);

    List<Room> findAll();

    Room findById(int id);

    Set<RoomLayout> getAllRoomLayoutsAvailableForThisRoom(int id);

    Set<Booking> getAllBookingOfThisRoom(int id);

    Room updateRoomById(int id, Room toBeCopied);
}
