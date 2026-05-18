package taskflow.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import taskflow.dto.LoginRequest;
import taskflow.dto.LoginResponse;
import taskflow.entity.User;
import taskflow.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private static final Logger logger =
            LogManager.getLogger(AuthService.class);

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setUsername(authentication.getName());
        user.setEmail(authentication.getName());
        return user;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            String token = jwtUtil.generateToken(
                    authentication.getName()
            );

            logger.info(
                    "LOGIN_SUCCESS user={}",
                    authentication.getName()
            );

            return new LoginResponse(token);


        } catch (AuthenticationException e) {
            logger.warn(
                    "LOGIN_FAILED user={}",
                    request.getUsername()
            );
            e.printStackTrace(); // 👈 MUY IMPORTANTE
            throw e;
        }
    }
}

