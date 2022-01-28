



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{

	User findByName(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByResetToken(String resetToken);

}
