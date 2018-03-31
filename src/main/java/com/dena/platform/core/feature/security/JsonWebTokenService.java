package com.dena.platform.core.feature.security;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.core.feature.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@Service("jwtService")
public class JsonWebTokenService implements TokenService {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenService.class);

    @Resource
    private DenaUserManagement userManagement;

    @Resource
    private DenaConfigReader configReader;

    private String secret;

    @PostConstruct
    private void init() {
        secret = DenaConfigReader.readProperty("dena.security.secret");
    }


    public String generate(String appId, User claimedUser) {
        String username = claimedUser.getEmail();
        String password = claimedUser.getUnencodedPassword();
        User user = userManagement.getUserById(appId, username);

        if (user != null && SecurityUtil.matchesPassword(password, user.getPassword())) {
            Claims claims = Jwts.claims()
                    .setSubject(username);

            claims.put("role", "fixed_role"); //TODO change role to user role
            claims.put("app_id", appId);

            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } else {
            throw new AuthenticationServiceException(String.format("not authenticated user: %s", username));
        }
    }


    @Override
    public User validate(String token) {
        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();

            user.setEmail(body.getSubject());
            //user.setRole((String) body.get("role"));
        } catch (Exception e) {
            LOGGER.error("not a valid token", e);
        }

        return user;
    }
}
