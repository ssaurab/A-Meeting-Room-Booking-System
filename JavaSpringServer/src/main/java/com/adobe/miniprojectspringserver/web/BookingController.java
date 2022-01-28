



import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    BookingService bookingService;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getBookingByID(@PathVariable("id") int id) {
        // todo: check if that booking id belongs to him or not
        Booking booking = bookingService.findById(id);
        if (booking != null) return new ResponseEntity<>(booking, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping(value = {""})
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.addNewBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

    }

    @GetMapping(value = {"/bookingslots/{date}/{roomId}"})
    public ResponseEntity<List<Pair<Integer, Integer>>> getBookingSlots(@PathVariable String date, @PathVariable("roomId") int roomId) {
        return new ResponseEntity<>(bookingService.findSlotBookings(date, roomId), HttpStatus.OK);
    }

}
