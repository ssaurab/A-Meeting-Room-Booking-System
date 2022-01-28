



import java.util.List;

public interface SnackService {
    Snack addNewSnack(Snack toBeAdded);

    Snack removeSnack(int id);

    List<Snack> findAll();

    Snack findById(int id);

    Snack updateById(Snack toBeAdded, int id);
}
