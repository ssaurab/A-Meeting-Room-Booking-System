



import java.util.List;

public interface EquipmentService {

    Equipment addNewEquipment(Equipment toBeAdded);

    Equipment removeEquipment(int id);

    List<Equipment> findAll();

    Equipment findById(int id);

    Equipment updateById(Equipment toBeAdded, int id);

}
