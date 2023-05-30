package se.ifmo.lab07.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.exception.RegisterException;
import se.ifmo.lab07.persistance.entity.User;
import se.ifmo.lab07.persistance.repository.UserRepository;
import se.ifmo.lab07.util.SecurityManager;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AuthManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthManager.class);

    private static final int BLOCKLIST_CLEARING_DELAY = 10_000;

    private static final long TOKEN_EXPIRATION_SECOND_TIME = 10;

    private static final String TOKEN_SECRET = "generated_for_training_purposes";

    private static final Algorithm ALGORITHM = Algorithm.HMAC512(TOKEN_SECRET);

    private final ConcurrentMap<String, Instant> blocklist = new ConcurrentHashMap<>();

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
                    .withExpiresAt(Instant.now().plusSeconds(TOKEN_EXPIRATION_SECOND_TIME))
                    .withClaim("username", username)
                    .sign(ALGORITHM);
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error(e.toString());
            throw new AuthorizationException("Something went wrong while authorization");
        }
    }

    public void logout(String token) {
        if (token != null) {
            var expTime = getExpirationTime(token);
            expTime.ifPresent(instant -> blocklist.put(token, instant));
        }
    }

    private void clearBlocklist() {
        for (var key : blocklist.keySet()) {
            if (blocklist.get(key).isBefore(Instant.now())) {
                blocklist.remove(key);
            }
        }
    }

    public static Optional<Instant> getExpirationTime(String token) {
        try {
            return Optional.of(getClaim(token, "exp", Date.class).toInstant());
        } catch (AuthorizationException e) {
            return Optional.empty();
        }

    }

    public Optional<String> getUsername(String token) {
        if (token == null) {
            return Optional.empty();
        }
        try {
            checkBlockList(token);
            return Optional.of(getClaim(token, "username", String.class));
        } catch (AuthorizationException ignored) {
            return Optional.empty();
        }
    }

    private void checkBlockList(String token) {
        if (blocklist.containsKey(token)) {
            throw new AuthorizationException("Token has been blocked");
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

    public void startClearing() {
        Runnable task = () -> {
            while (true) {
                try {
                    logger.info(blocklist.toString());
                    clearBlocklist();
                    Thread.sleep(BLOCKLIST_CLEARING_DELAY);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };
        var thread = new Thread(task);
        thread.setName("blocklist-cleaner");
        thread.setDaemon(true);
        thread.start();
    }
}
