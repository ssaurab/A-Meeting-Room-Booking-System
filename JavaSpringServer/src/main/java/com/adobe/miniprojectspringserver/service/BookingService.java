




import javafx.util.Pair;

import java.util.List;

public interface BookingService {
    Booking addNewBooking(Booking toBeAdded);

    Booking removeBooking(int id);

    List<Booking> findAll();

    Booking findById(int id) throws IllegalArgumentNotFoundException;

    Booking editBookingById(int id, Booking toBeCopied);

    void sendBookingEmail(Booking finalToBeAdded);

    void sendStatusEmail(String oldStatus, String newStatus, Booking finalToBeAdded);

    Long countBookedRoomByRoomId(int id);

    List<Pair<Integer, Integer>> findSlotBookings(String date, int roomId); // YYYY-MM-DD
}
