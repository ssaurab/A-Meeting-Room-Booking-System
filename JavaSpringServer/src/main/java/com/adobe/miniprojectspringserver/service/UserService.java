



import java.util.List;
import java.util.Optional;

public interface UserService {
    User addNewUser(User toBeAdded);

    User removeUser(int id);

    List<User> findAll();

    User findById(int id);

    User editUserById(int id, User toBeCopied);

    Object isLogged();

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByResetToken(String resetToken);
}
