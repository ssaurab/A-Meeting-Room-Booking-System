





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userServiceDao;

    @Autowired
    public void setDao(UserRepository dao) {
        this.userServiceDao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userServiceDao.findByName(username);
        UserDetailsImpl userDetails;
        if (user != null) {
            userDetails = new UserDetailsImpl();
            userDetails.setUser(user);
        } else
            throw new UsernameNotFoundException("User does not exist with the name " + username);
        return userDetails;
    }

    @Override
    public User addNewUser(User toBeAdded) throws IllegalArgumentBadRequestException {
        UserValidity uv = new UserValidity(toBeAdded);

        if (!uv.checkValidity()) {
            throw new IllegalArgumentBadRequestException(
                    "Invalid " + (!uv.roleIsValid ? "role " : "") + (!uv.emailIsValid ? "email " : "")
                            + (!uv.phoneIsValid ? "phone " : "") + (!uv.statusIsValid ? "status " : "")
                            + (!uv.passwordIsValid ? "password " : "") + "field(s) for User argument");
        } else {
            return userServiceDao.save(toBeAdded);
        }
    }

    @Override
    public User removeUser(int id) throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        try {
            Object loggedUser = isLogged();
            if ((loggedUser != null) && ((User) loggedUser).getId() == id)
                throw new IllegalArgumentBadRequestException("You cannot delete your own account.");
        } catch (NullPointerException e) {
        }

        Optional<User> existing = userServiceDao.findById(id);
        if (existing.isPresent()) {
            userServiceDao.deleteById(id);
            return existing.get();

        }
        throw new IllegalArgumentNotFoundException("User not found!!");
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userServiceDao.findAll();
    }

    @Override
    public User findById(int id) throws IllegalArgumentNotFoundException {
        Optional<User> userWithId = userServiceDao.findById(id);
        if (userWithId.isPresent())
            return userWithId.get();
        throw new IllegalArgumentNotFoundException("User not found!!");
    }

    @Override
    public User editUserById(int id, User toBeCopied)
            throws IllegalArgumentNotFoundException, IllegalArgumentBadRequestException {
        UserValidity uv = new UserValidity(toBeCopied);
        if (!uv.checkValidity()) {
            throw new IllegalArgumentBadRequestException(
                    "Invalid " + (!uv.roleIsValid ? "role " : "") + (!uv.emailIsValid ? "email " : "")
                            + (!uv.phoneIsValid ? "phone " : "") + (!uv.statusIsValid ? "status " : "")
                            + (!uv.passwordIsValid ? "password " : "") + "field(s) for User argument");
        }

        User existing = this.findById(id);
        int existing_id = existing.getId();
        existing = toBeCopied;
        existing.setId(existing_id);
        return userServiceDao.save(existing);
    }

    public Object isLogged() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetailsImpl) principal).getUsername();
        } else {
            username = principal.toString().split(",")[0].split("=")[1];
        }
        if (username != null && username.length() > 0)
            return userServiceDao.findByName(username);
        else
            return null;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userServiceDao.findByEmail(email);
        System.out.println("user is" + user);
        return user;
    }

    @Override
    public Optional<User> findUserByResetToken(String resetToken) {
        return userServiceDao.findByResetToken(resetToken);
    }

    class UserValidity {
        boolean roleIsValid, emailIsValid, phoneIsValid, statusIsValid, passwordIsValid;

        public UserValidity(User user) {
            roleIsValid = user.getRole().equals("administrator") || user.getRole().equals("editor");
            emailIsValid = (/* user.getEmail() != "" && */user.getEmail().contains("@"));
            phoneIsValid = (/* user.getPhone() != "" && */(user.getPhone().length() == 10
                    && user.getPhone().matches("[0-9]+"))); // Check that phone number has length 10 and contains only
            // digits
            statusIsValid = (user.getStatus().equals("active") || user.getStatus().equals("inactive"));
            passwordIsValid = (!user.getPassword().equals(user.getPassword().toLowerCase())
                    && !user.getPassword().equals(user.getPassword().toUpperCase())
                    && user.getPassword().matches(".*[0-9].*") && user.getPassword().length() >= 8);
        }

        public boolean checkValidity() {
            return roleIsValid && emailIsValid && phoneIsValid && statusIsValid && passwordIsValid;
        }

    }

}
