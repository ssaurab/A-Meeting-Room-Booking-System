
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword(), new ArrayList<>()));
        } catch (Exception e) {
            try {
                res.setStatus(403);
                res.getWriter().write("{\"message\" :  \"Invalid credentials!\"} ");
                res.flushBuffer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        try {
            Date exp = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
            Key key = Keys.hmacShaKeyFor(SecurityConstants.KEY.getBytes());
            UserDetailsImpl temp = ((UserDetailsImpl) auth.getPrincipal());
            Claims claims = Jwts.claims().setSubject(temp.getUsername());
            String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).setExpiration(exp).compact();
            if (temp.getUser().getStatus().equalsIgnoreCase("active")) {
                res.addHeader("token", token);
                temp.getUser().setPassword(null);
                String json = new ObjectMapper().writeValueAsString(temp.getUser());
                res.getWriter().write(json);
                res.flushBuffer();
            } else {
                res.setStatus(403);
                res.getWriter().write("{\"message\" :  \"This user's status is inactive!\"} ");
                res.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}