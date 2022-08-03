package de.unistuttgart.chickenshockbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorizationService {

  public boolean isAuthorized(final String tokenCookie) {
    boolean authorized = false;
    try {
      final Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
      final JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
      final DecodedJWT jwt = verifier.verify(tokenCookie);
      authorized = true;
      log.debug("verification successfully! id was: {}", jwt.getClaim("id"));
    } catch (JWTVerificationException exception) {
      log.debug("verification not successfully.", exception);
    }
    return authorized;
  }

  public String getPlayerId(final String tokenCookie) {
    try {
      final Algorithm algorithm = Algorithm.HMAC256("test"); //use more secure key
      final JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
      final DecodedJWT jwt = verifier.verify(tokenCookie);
      log.debug("verification successfully! id was: {}", jwt.getClaim("id"));
      return jwt.getClaim("id").toString();
    } catch (JWTVerificationException exception) {
      log.debug("verification not successfully.", exception);
    }
    return "no ID for this player found";
  }
}
