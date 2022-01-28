






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {


    private EquipmentRepository EquipmentsServiceDao;

    @Autowired
    public void setEquipmentsServiceDao(EquipmentRepository EquipmentsServiceDao) {
        this.EquipmentsServiceDao = EquipmentsServiceDao;
    }

    private String validateEquipment(Equipment toBeAdded) {
        if (toBeAdded.getPrice() < 0)
            return "price can't be 0";
        else if (toBeAdded.getTitle().length() == 0)
            return "Name of Equipment is missing!";
        else if (!(new ArrayList<>(Arrays.asList("perHour", "perBooking"))).contains(toBeAdded.getPriceType()))
            return "Invalid Parameter to PriceType";
        else
            return "valid";
    }

    @Override
    public Equipment addNewEquipment(Equipment toBeAdded) throws IllegalArgumentBadRequestException {
        String validateMsg = validateEquipment(toBeAdded);
        if (validateMsg.equals("valid")) {
            return EquipmentsServiceDao.save(toBeAdded);
        }
        throw new IllegalArgumentBadRequestException(validateMsg);
    }

    @Override
    public Equipment removeEquipment(int id) throws IllegalArgumentNotFoundException {
        Optional<Equipment> existing = EquipmentsServiceDao.findById(id);
        if (existing.isPresent()) {
            List<BookingEquipments> bookings = existing.get().getBookings();
            if (!bookings.isEmpty()) {
                throw new IllegalArgumentBadRequestException("Cannot delete an Equipment required for a Booking.");
            }
            EquipmentsServiceDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("Equipment not found!!");
    }

    @Override
    public List<Equipment> findAll() {
        return (List<Equipment>) EquipmentsServiceDao.findAll();
    }

    @Override
    public Equipment findById(int id) throws IllegalArgumentNotFoundException {
        Optional<Equipment> EquipmentsWithId = EquipmentsServiceDao.findById(id);
        if (EquipmentsWithId.isPresent())
            return EquipmentsWithId.get();
        throw new IllegalArgumentNotFoundException("Equipment not found!!");
    }

    @Override
    public Equipment updateById(Equipment toBeAdded, int id) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        String validateMsg = validateEquipment(toBeAdded);
        if (!(validateMsg == "valid")) {
            throw new IllegalArgumentBadRequestException(validateMsg);
        }

        Optional<Equipment> existing = EquipmentsServiceDao.findById(id);
        if (existing.isPresent()) {
            Equipment existingEquipment = existing.get();
            int oldId = existingEquipment.getId();
            existingEquipment = toBeAdded;
            existingEquipment.setId(oldId);
            return EquipmentsServiceDao.save(existingEquipment);
        }
        throw new IllegalArgumentNotFoundException("Equipment not found!!");
    }


}
