



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/V1/users")
public class UserControllerV1 {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = {""})
    public ResponseEntity<?> getAllUsersV1() {
        Object loggedUser = userService.isLogged();
        if (loggedUser == null || ((User) loggedUser).getRole().equals("editor"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }


    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getUserByID(@PathVariable("id") int id) {
        Object loggedUser = userService.isLogged();
        if (loggedUser == null || ((User) loggedUser).getRole().equals("editor"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = userService.findById(id);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = {""})
    public ResponseEntity<?> addUser(@RequestBody User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Object loggedUser = userService.isLogged();
        if (loggedUser == null || ((User) loggedUser).getRole().equals("editor"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String pwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pwd));
        User newUser = userService.addNewUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        Object loggedUser = userService.isLogged();
        if (loggedUser == null || ((User) loggedUser).getRole().equals("editor"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = userService.removeUser(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<?> editUserById(@PathVariable("id") int id, @RequestBody User user) {
        Object loggedUser = userService.isLogged();
        if (loggedUser == null || ((User) loggedUser).getRole().equals("editor"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pwd));
        User editedUser = userService.editUserById(id, user);
        if (editedUser != null)
            return new ResponseEntity<>(editedUser, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = {"/status/{id}/{status}"})
    public ResponseEntity<?> editUserStatusById(@PathVariable("id") int id, @PathVariable("status") String status) {
        try {
            User toBeEditedUser = userService.findById(id);
            if (toBeEditedUser == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                toBeEditedUser.setStatus(status);
                userService.editUserById(id, toBeEditedUser);
                return new ResponseEntity<>(toBeEditedUser, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("User not found!!")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } else { // User argument was wrong
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }
    }

    @GetMapping(value = {"/currentUser"})
    public ResponseEntity<?> isLoggedIn() {
        Object loggedUser = userService.isLogged();
        if (loggedUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            User loggedInUser = (User) loggedUser;
            loggedInUser.setPassword(null);
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        }

    }

    @PostMapping(value = {"/changePassword"})
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        HashMap<String, String> map = new HashMap<>();
        String newPassword = payload.get("newPassword");
        String currentPassword = payload.get("currentPassword");
        if (currentPassword == null || newPassword == null) {
            map.put("message", "Missing some parameters");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (newPassword.equals(newPassword.toLowerCase())) {
            map.put("message", "Password must contain a uppercase letter");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (newPassword.length() < 8) {
            map.put("message", "Password must be 8 character long!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (newPassword.equals(newPassword.toUpperCase())) {
            map.put("message", "Password must contain a lowercase letter");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (!newPassword.matches(".*[0-9].*")) {
            map.put("message", "Password must contain at least 1 digit");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Object user = userService.isLogged();
        if (user != null) {
            User loggedInUser = (User) user;
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
                loggedInUser.setPassword(passwordEncoder.encode(newPassword));
                userService.editUserById(loggedInUser.getId(), loggedInUser);
                map.put("message", "Password changed successfully!");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("message", "Current password is wrong!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put("message", "User doesn't exist!");
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

}
