/**
 * 
 */


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface SnackRepository extends CrudRepository<Snack, Integer>{
}
