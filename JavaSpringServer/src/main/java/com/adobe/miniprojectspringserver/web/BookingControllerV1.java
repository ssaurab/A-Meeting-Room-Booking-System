




import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/V1/bookings")
public class BookingControllerV1 {
    BookingService bookingService;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<List<?>> getAllBookingsV1() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }


    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getBookingByIDV1(@PathVariable("id") int id) {
        Booking booking = bookingService.findById(id);
        if (booking != null) return new ResponseEntity<>(booking, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = {"/bookingson/{date}"})
    public ResponseEntity<List<?>> getAllBookingsOnDateV1(@PathVariable("date") String date) {
        List<Booking> bookings = bookingService.findAll().stream().filter(booking -> booking.getDate().equals(date)).collect(Collectors.toList());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }


    @PostMapping(value = {""})
    public ResponseEntity<?> addBookingV1(@RequestBody Booking booking) {
        Booking newBooking = bookingService.addNewBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteBookingV1(@PathVariable("id") int id) {
        Booking booking = bookingService.removeBooking(id);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> editBookingByIdV1(@PathVariable("id") int id, @RequestBody Booking booking) {
        booking.setId(0);
        if (booking.getClient() == null) {
            throw new IllegalArgumentBadRequestException("Booking client can't be null");
        }
        booking.getClient().setId(0);
        Booking removed = bookingService.removeBooking(id);
        if (removed == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Booking newBooking = bookingService.addNewBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.OK);
    }

    @PutMapping(value = {"/status/{id}/{status}"})
    public ResponseEntity<?> editBookingStatusByIdV1(@PathVariable("id") int id, @PathVariable("status") String status) {
        Booking toBeEdited = bookingService.findById(id);
        if (toBeEdited == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            toBeEdited.setStatus(status);
            bookingService.editBookingById(id, toBeEdited);
            return new ResponseEntity<>(toBeEdited, HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/countroom/{id}"})
    public Long countRoomByIdV1(@PathVariable("id") int id) {
        return bookingService.countBookedRoomByRoomId(id);
    }

    @GetMapping(value = {"/bookingslots/{date}/{roomId}"})
    public ResponseEntity<List<Pair<Integer, Integer>>> getBookingSlotsV1(@PathVariable String date, @PathVariable("roomId") int roomId) {
        return new ResponseEntity<>(bookingService.findSlotBookings(date, roomId), HttpStatus.OK);
    }

    @GetMapping(value = {"/countofbookingstoday",})
    public ResponseEntity<ArrayList<Integer>> GetVariousCountsOfBookingsV1() {
        ArrayList<Integer> response = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        List<Booking> bookings = bookingService.findAll();
        List<Booking> filteredBookings = bookingService.findAll().stream().filter(booking -> fmt.format(booking.getRegDate()).equals(fmt.format(new Date()))).collect(Collectors.toList());
        response.add(bookings.size());
        response.add(filteredBookings.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
