




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @PostMapping(value = {"/forgotPassword"})
    public ResponseEntity<?> processForgotPasswordForm(@RequestBody Map<String, String> payload,
                                                       HttpServletRequest request) {
        String email = payload.get("email");
        HashMap<String, String> map = new HashMap<>();
        if (email == null) {
            map.put("message", "Email is missing!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Optional<User> optionalUser = userService.findUserByEmail(email);
        String appUrl = request.getHeader("referer") + "/reset?token=";

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(optionalUser, HttpStatus.NOT_FOUND);
        } else {
            User user = optionalUser.get();
            String token = user.getResetToken();
            long minPassed = 0;
            if (token == null) {
                token = UUID.randomUUID() + "-" + Long.toHexString(System.currentTimeMillis());
            } else {
                String ts = token.substring(token.lastIndexOf('-') + 1);
                minPassed = (System.currentTimeMillis() - Long.parseLong(ts, 16)) / (1000 * 60);
                if (minPassed > 15)
                    token = UUID.randomUUID() + "-" + Long.toHexString(System.currentTimeMillis());
            }
            user.setResetToken(token);
            userService.editUserById(user.getId(), user);
            String finalToken = token;
            long finalMinPassed = minPassed;
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                boolean multipart = true;
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart, "utf-8");
                String htmlMsg = "Visit the link below to reset your password. The link is valid for "
                        + (15 - finalMinPassed) + " minutes:<br>" + "<a href=" + appUrl + finalToken
                        + ">Reset password</a>";
                helper.setTo(user.getEmail());
                helper.setSubject("Password Reset");
                mimeMessage.setContent(htmlMsg, "text/html");
            };
            emailService.sendEmail(messagePreparator);
        }
        map.put("message", "Please check your email for temporary password required to reset the password!");
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity<?> setNewPassword(@RequestBody Map<String, String> payload) {
        HashMap<String, String> map = new HashMap<>();
        String token = payload.get("token");
        String password = payload.get("password");
        if (token == null || password == null) {
            map.put("message", "Missing some parameters");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (password.equals(password.toLowerCase())) {
            map.put("message", "Password must contain a uppercase letter");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (password.length() < 8) {
            map.put("message", "Password must be 8 character long!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (password.equals(password.toUpperCase())) {
            map.put("message", "Password must contain a lowercase letter");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else if (!password.matches(".*[0-9].*")) {
            map.put("message", "Password must contain at least 1 digit");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        String ts = token.substring(token.lastIndexOf('-') + 1);
        Long minPassed = (System.currentTimeMillis() - Long.parseLong(ts, 16)) / (1000 * 60);
        if (minPassed > 15) {
            map.put("message", "The link seems to have expired already!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<User> user = userService.findUserByResetToken(payload.get("token"));
        if (user.isPresent()) {
            User resetUser = user.get();
            resetUser.setPassword(passwordEncoder.encode(password));
            resetUser.setResetToken(null);
            userService.editUserById(resetUser.getId(), resetUser);
            map.put("message", "Password has been reset. please login!");
            return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
        } else {
            map.put("message", "Invalid reset link!");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

}
