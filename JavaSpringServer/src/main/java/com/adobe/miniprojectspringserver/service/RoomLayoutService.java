





import java.util.List;
import java.util.Set;

public interface RoomLayoutService {
    RoomLayout addNewRoomLayout(RoomLayout toBeAdded);

    RoomLayout removeRoomLayout(int id);

    List<RoomLayout> findAll();

    RoomLayout findById(int id);

    Set<Room> getAllRoomAvailableForThisRoomLayout(int id);

    RoomLayout updateRoomLayoutById(int id, RoomLayout toBeCopied);
}