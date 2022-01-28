







import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepositoryDao;
    EmailService emailService;

    @Autowired
    public void setDao(BookingRepository bookingRepositoryDao) {
        this.bookingRepositoryDao = bookingRepositoryDao;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public String validateBookingData(Booking toBeAdded) {
        if (toBeAdded.getClient() == null)
            return "Booking client can't be empty";
        else if (toBeAdded.getRoom() == null)
            return "Booking Room can't be null";
        else if (toBeAdded.getRoomLayout() == null)
            return "Booking Room Layout can't be empty";
        else if (toBeAdded.getAttendees() <= 0)
            return "At least 1 attendee required";
        else if (!(new ArrayList<>(Arrays.asList("authorize.net", "wire transfer", "cash", "credit card", "paypal")))
                .contains(toBeAdded.getPaymentMethod().toLowerCase()))
            return "Invalid Payment Method";
        else if (!(new ArrayList<>(Arrays.asList("confirmed", "pending", "cancelled")))
                .contains(toBeAdded.getStatus().toLowerCase()))
            return "Invalid Status";
        else if (toBeAdded.getRoom().getStatus().equalsIgnoreCase("inactive"))
            return "Inactive room can't be booked";
        else if (toBeAdded.getDate()
                .compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).substring(0, 10)) < 0)
            return "You cannot modify or make a booking of a past date!";
        else if (toBeAdded.getAttendees() > toBeAdded.getRoom().getCapacity())
            return "Number of attendees can't be greater than room capacity";
        else if (toBeAdded.getFromHour() >= toBeAdded.getToHour())
            return "FromHour should be less than ToHour";
        else
            return "valid";
    }

    @Override
    public Booking addNewBooking(Booking toBeAdded) throws IllegalArgumentBadRequestException {
        String validationMsg = validateBookingData(toBeAdded);
        if (validationMsg.equals("valid")) {
            List<BookingEquipments> equip = toBeAdded.getEquipments();
            Float equipmentPrice = (float) 0;
            for (BookingEquipments e : equip) {
                if (e.getEquipments().getPriceType() == "perBooking")
                    equipmentPrice += e.getEquipments().getPrice() * e.getCount();
                else { // PriceType is "perHour"
                    equipmentPrice += e.getEquipments().getPrice() * e.getCount()
                            * (toBeAdded.getToHour() - toBeAdded.getFromHour());
                }
            }

            List<BookingSnacks> snacks = toBeAdded.getSnacks();
            Float snackPrice = (float) 0;
            for (BookingSnacks s : snacks) {
                snackPrice += s.getSnacks().getPrice() * s.getCount();
            }
            int from = toBeAdded.getFromHour();
            int to = toBeAdded.getToHour();
            int totalHour = to - from;

            Float roomPrice = (float) 0;
            if ((from == 8 && to == 12) || (from == 13 && to == 17)) {
                roomPrice = totalHour * toBeAdded.getRoom().getPricePerHalfDay();
            } else {
                roomPrice = totalHour * toBeAdded.getRoom().getPricePerHour();
            }

            Float subTotal = equipmentPrice + snackPrice + roomPrice;

            Float tax = (float) (0.1 * subTotal);
            Float total = (float) (1.1 * subTotal);
            Float deposit = (float) (0.1 * total);

            toBeAdded.setDeposit(deposit);
            toBeAdded.setEquipmentPrice(equipmentPrice);
            toBeAdded.setFoodAndDrinkPrice(snackPrice);
            toBeAdded.setTax(tax);
            toBeAdded.setSubTotal(subTotal);
            toBeAdded.setTotal(total);
            toBeAdded.setRoomPrice(roomPrice);

            for (BookingEquipments temp : toBeAdded.getEquipments()) {
                temp.setBooking(toBeAdded);
            }
            for (BookingSnacks temp : toBeAdded.getSnacks()) {
                temp.setBooking(toBeAdded);
            }
            toBeAdded = bookingRepositoryDao.save(toBeAdded);
            sendBookingEmail(toBeAdded);
            return toBeAdded;

        } else {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }
    }

    public void sendBookingEmail(Booking finalToBeAdded) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart, "utf-8");
            String htmlMsg = "<h3>Booking Details</h3>"
                    + "<ul style=\"margin:0; margin-left: 25px; padding:0; font-family: Arial, sans-serif; color:#495055; font-size:16px; line-height:22px;\" align=\"left\" type=\"disc\">\n"
                    + "<li>" + "        	Name				:			 " + finalToBeAdded.getClient().getName()
                    + "</li>" + "<li>" + "        	Date of Booking		:			 " + finalToBeAdded.getDate()
                    + "</li>" + "<li>" + "        	Number of attendees :			 " + finalToBeAdded.getAttendees()
                    + "</li>" + "<li>" + "        	Room				:			 " + finalToBeAdded.getRoom().getName()
                    + "</li>" + "<li>" + "        	Room layout			:			 " + finalToBeAdded.getRoomLayout().getTitle()
                    + "</li>" + "<li>" + "        	Status				:			 " + finalToBeAdded.getStatus() + "</li>"
                    + "<li>" + "        	Total				:			 " + finalToBeAdded.getTotal() + "</li>"
                    + "<li>" + "        	Deposit				:			 " + finalToBeAdded.getDeposit() + "</li>"
                    + "</ul>";

            helper.setTo(finalToBeAdded.getClient().getEmail());
            helper.setSubject("Booking Summary");
            mimeMessage.setContent(htmlMsg, "text/html");
        };
        emailService.sendEmail(messagePreparator);
    }

    public void sendStatusEmail(String oldStatus, String newStatus, Booking finalToBeAdded) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart, "utf-8");
            String htmlMsg = "<h3>Booking Details</h3>"
                    + "<ul style=\"margin:0; margin-left: 25px; padding:0; font-family: Arial, sans-serif; color:#495055; font-size:16px; line-height:22px;\" align=\"left\" type=\"disc\">\n"
                    + "<li>" + "        	Name				:			 " + finalToBeAdded.getClient().getName()
                    + "</li>" + "<li>" + "        	Date of Booking		:			 " + finalToBeAdded.getDate()
                    + "</li>" + "<li>" + "        	Number of attendees :			 " + finalToBeAdded.getAttendees()
                    + "</li>" + "<li>" + "        	Room				:			 " + finalToBeAdded.getRoom().getName()
                    + "</li>" + "<li>" + "        	Room layout			:			 " + finalToBeAdded.getRoomLayout().getTitle()
                    + "</li>" + "<li>" + "        	Status changed from: " + oldStatus + " ==> " + newStatus + "</li>"
                    + "<li>" + "        	Total				:			 " + finalToBeAdded.getTotal() + "</li>"
                    + "<li>" + "        	Deposit				:			 " + finalToBeAdded.getDeposit() + "</li>"
                    + "</ul>";

            helper.setTo(finalToBeAdded.getClient().getEmail());
            helper.setSubject("Booking Status changed.");
            mimeMessage.setContent(htmlMsg, "text/html");
        };
        emailService.sendEmail(messagePreparator);
    }

    @Override
    public Booking removeBooking(int id) throws IllegalArgumentNotFoundException {
        Optional<Booking> existing = bookingRepositoryDao.findById(id);
        if (existing.isPresent()) {
            bookingRepositoryDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("Booking not found!!");
    }

    @Override
    public List<Booking> findAll() {
        return (List<Booking>) bookingRepositoryDao.findAll();
    }

    @Override
    public Booking findById(int id) throws IllegalArgumentNotFoundException {
        Optional<Booking> bookingWithId = bookingRepositoryDao.findById(id);
        if (bookingWithId.isPresent())
            return bookingWithId.get();
        throw new IllegalArgumentNotFoundException("Booking not found!!");
    }

    @Override
    public Booking editBookingById(int id, Booking toBeCopied)
            throws IllegalArgumentBadRequestException, IllegalArgumentNotFoundException {
        String validationMsg = validateBookingData(toBeCopied);
        if (!(validationMsg.equals("valid"))) {
            throw new IllegalArgumentBadRequestException(validationMsg);
        }
        Optional<Booking> bookingWithId = bookingRepositoryDao.findById(id);
        if (bookingWithId.isPresent()) {
            Booking existing = bookingWithId.get();
            int existing_id = existing.getId();
            existing = toBeCopied;
            existing.setId(existing_id);
            return bookingRepositoryDao.save(existing);
        }
        throw new IllegalArgumentNotFoundException("Booking not found!!");
    }

    @Override
    public Long countBookedRoomByRoomId(int id) {
        return bookingRepositoryDao.countByRoomId(id);
    }

    @Override
    public List<Pair<Integer, Integer>> findSlotBookings(String date, int roomId) {
        List<Booking> bookings = findAll();
        List<Booking> filteredBookings = bookings.stream()
                .filter(b -> (b.getDate().substring(0, 10).equals(date) && b.getRoom().getId() == roomId))
                .collect(Collectors.toList());
        List<Pair<Integer, Integer>> requiredSlots = new ArrayList<Pair<Integer, Integer>>();
        for (Booking b : filteredBookings) {
            requiredSlots.add(new Pair<Integer, Integer>(b.getFromHour(), b.getToHour()));
        }
        return requiredSlots;
    }

}
