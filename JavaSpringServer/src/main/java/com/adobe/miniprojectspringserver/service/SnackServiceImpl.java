






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnackServiceImpl implements SnackService {


    private SnackRepository SnackServiceDao;

    @Autowired
    public void setFoodAndDrinksServiceDao(SnackRepository FoodAndDrinksServiceDao) {
        this.SnackServiceDao = FoodAndDrinksServiceDao;
    }

    public String validateSnack(Snack toBeAdded) {
        if (toBeAdded.getPrice() < 0)
            return "Price can't be non-positive";
        else if (toBeAdded.getTitle().length() == 0)
            return "Name of snack is required";
        else
            return "valid";
    }

    @Override
    public Snack addNewSnack(Snack toBeAdded) throws IllegalArgumentBadRequestException {
        String validateMsg = validateSnack(toBeAdded);
        if (validateMsg.equals("valid")) {
            return SnackServiceDao.save(toBeAdded);
        }
        throw new IllegalArgumentBadRequestException(validateMsg);
    }

    @Override
    public Snack removeSnack(int id) throws IllegalArgumentNotFoundException {
        Optional<Snack> existing = SnackServiceDao.findById(id);
        if (existing.isPresent()) {
            List<BookingSnacks> bookings = existing.get().getBookings();
            if (!bookings.isEmpty()) {
                throw new IllegalArgumentBadRequestException("Cannot delete a Snack required for a Booking.");
            }
            SnackServiceDao.deleteById(id);
            return existing.get();
        }
        throw new IllegalArgumentNotFoundException("Snack not found!!");
    }

    @Override
    public List<Snack> findAll() {
        return (List<Snack>) SnackServiceDao.findAll();
    }

    @Override
    public Snack findById(int id) throws IllegalArgumentNotFoundException {
        Optional<Snack> SnackWithId = SnackServiceDao.findById(id);
        if (SnackWithId.isPresent())
            return SnackWithId.get();
        throw new IllegalArgumentNotFoundException("Snack not found!!");
    }

    @Override
    public Snack updateById(Snack toBeAdded, int id) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        String validateMsg = validateSnack(toBeAdded);
        if (!(validateMsg == "valid")) {
            throw new IllegalArgumentBadRequestException(validateMsg);
        }
        Optional<Snack> existing = SnackServiceDao.findById(id);
        if (existing.isPresent()) {
            Snack existingFood = existing.get();
            int oldId = existingFood.getId();
            existingFood = toBeAdded;
            existingFood.setId(oldId);
            return SnackServiceDao.save(existingFood);
        }
        throw new IllegalArgumentNotFoundException("Snack not found!!");
    }

}
