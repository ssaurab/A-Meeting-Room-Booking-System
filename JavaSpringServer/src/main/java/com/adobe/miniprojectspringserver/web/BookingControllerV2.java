





import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/V2/bookings")
public class BookingControllerV2 {
    @Autowired
    EmailService emailService;

    BookingService bookingService;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<?> getAllBookingsV2(
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortingParam,
            @RequestParam(name = "from", required = false, defaultValue = "1") String fromString,
            @RequestParam(name = "to", required = false, defaultValue = Integer.MAX_VALUE + "") String toString,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<Booking> bookings = bookingService.findAll();
        int from, to;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) { // If from is not a valid integer, return from start of list
            from = 1;
        }
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) { // If to is not a valid integer, return till end of list
            to = bookings.size();
        }
        if (from > to) {
            return new ResponseEntity<>("From should be LTEQ To", HttpStatus.BAD_REQUEST);
        }
        from--;
        order = (order.equals("asc") || order.equals("des")) ? order : "asc";
        switch (sortingParam) {
            case "date":
                bookings.sort(Comparator.comparing(Booking::getDate));
                break;
            case "name":
                bookings.sort(Comparator.comparing(Booking::getClient, Comparator.comparing(Client::getName)));
                break;
            case "total":
                bookings.sort(Comparator.comparing(Booking::getTotal));
                break;
            case "status":
                bookings.sort(Comparator.comparing(Booking::getStatus));
                break;
            default:
                bookings.sort(Comparator.comparing(Booking::getId));
                break;
        }
        if (order.equals("des"))
            Collections.reverse(bookings);
        if (from > bookings.size())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        else if (to > bookings.size())
            return new ResponseEntity<>(bookings.subList(from, bookings.size()), HttpStatus.OK);
        else
            return new ResponseEntity<>(bookings.subList(from, to), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getBookingByIDV2(@PathVariable("id") int id) {
        Booking booking = bookingService.findById(id);
        if (booking != null) return new ResponseEntity<>(booking, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = {"/bookingson/{date}"})
    public ResponseEntity<?> getAllBookingsOnDateV2(@PathVariable("date") String date) {
        List<Booking> bookings = bookingService.findAll().stream().filter(booking -> booking.getDate().equals(date)).collect(Collectors.toList());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }


    @PostMapping(value = {""})
    public ResponseEntity<?> addBookingV2(@RequestBody Booking booking) {
        Booking newBooking = bookingService.addNewBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteBookingV2(@PathVariable("id") int id) {
        Booking booking = bookingService.removeBooking(id);
        if (booking != null) return new ResponseEntity<>(booking, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> editBookingByIdV2(@PathVariable("id") int id, @RequestBody Booking booking) {
        booking.setId(0);
        booking.getClient().setId(0);
        Booking removed = bookingService.removeBooking(id);
        if (removed == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Booking newBooking = bookingService.addNewBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.OK);
    }

    @PatchMapping(value = {"/{id}"})
    public ResponseEntity<?> patchBookingById(@PathVariable("id") int id, @RequestBody Map<Object, Object> payload) {
        Booking booking = bookingService.findById(id);
        if (booking == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        payload.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Booking.class, (String) k);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.toLowerCase().contains("float"))
                    ReflectionUtils.setField(field, booking, Float.parseFloat(v.toString()));
                else {
                    if (k.equals("status") && !v.equals(booking.getStatus())) {
                        String oldStatus = booking.getStatus();
                        bookingService.sendStatusEmail(oldStatus, (String) v, booking);
                    }
                    ReflectionUtils.setField(field, booking, v);
                }
            }
        });
        bookingService.editBookingById(id, booking);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PutMapping(value = {"/status/{id}/{status}"})
    public ResponseEntity<?> editBookingStatusByIdV2(@PathVariable("id") int id, @PathVariable("status") String status) {
        Booking toBeEdited = bookingService.findById(id);
        if (toBeEdited == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            String oldStatus = toBeEdited.getStatus();
            if (toBeEdited.getStatus().equals(status)) return new ResponseEntity<>(toBeEdited, HttpStatus.OK);
            toBeEdited.setStatus(status);
            bookingService.editBookingById(id, toBeEdited);
            bookingService.sendStatusEmail(oldStatus, status, toBeEdited);
            return new ResponseEntity<>(toBeEdited, HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/countroom/{id}"})
    public Long countRoomByIdV2(@PathVariable("id") int id) {
        return bookingService.countBookedRoomByRoomId(id);
    }

    @GetMapping(value = {"/bookingslots/{date}/{roomId}"})
    public ResponseEntity<List<Pair<Integer, Integer>>> getBookingSlotsV2(@PathVariable String date, @PathVariable("roomId") int roomId) {
        return new ResponseEntity<>(bookingService.findSlotBookings(date, roomId), HttpStatus.OK);
    }

    @GetMapping(value = {"/countofbookingstoday"})
    public ResponseEntity<ArrayList<Integer>> GetVariousCountsOfBookingsV2() {
        ArrayList<Integer> response = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        List<Booking> bookings = bookingService.findAll();
        List<Booking> filteredBookings = bookingService.findAll().stream().filter(booking -> fmt.format(booking.getRegDate()).equals(fmt.format(new Date()))).collect(Collectors.toList());
        response.add(bookings.size());
        response.add(filteredBookings.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
