package se.ifmo.lab07.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.entity.User;
import se.ifmo.lab07.persistance.repository.UserRepository;
import se.ifmo.lab07.util.SecurityManager;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthManager.class);

    private static final String TOKEN_SECRET = "generated_for_training_purposes";

    private static final Algorithm ALGORITHM = Algorithm.HMAC512(TOKEN_SECRET);

    private final UserRepository userRepository = new UserRepository();

    public void register(String username, String password) {
        var salt = UUID.randomUUID().toString();
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new AuthorizationException("User already exists");
            }
            var hash = SecurityManager.generateHash(password, salt);
            var user = new User(username, hash, salt);
            userRepository.save(user);
        } catch (NoSuchAlgorithmException | SQLException e) {
            logger.error(e.toString());
            throw new AuthorizationException("Something went wrong while registration");
        }
    }

    public String authorize(String username, String password) {
        try {
            var user = userRepository.findByUsername(username).orElseThrow(() -> new AuthorizationException("Username not found"));
            if (!SecurityManager.checkPasswordHash(user.password(), password, user.salt())) {
                throw new AuthorizationException("Invalid password");
            }
            return JWT.create()
                    .withClaim("username", username)
                    .sign(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
            throw new AuthorizationException("Something went wrong while authorization");
        }
    }

    public Optional<String> getUsername(String token) {
        if (token == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(getClaim(token, "username", String.class));
        } catch (AuthorizationException e) {
            logger.error(e.toString());
            return Optional.empty();
        }
    }

    private static <T> T getClaim(String token, String claim, Class<T> clazz) {
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM)
                    .build()
                    .verify(token);
            return jwt.getClaim(claim).as(clazz);
        } catch (JWTVerificationException e) {
            logger.error(e.toString());
            throw new AuthorizationException("Invalid or expired token");
        }
    }
}
