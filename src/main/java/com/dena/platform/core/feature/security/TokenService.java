package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.user.domain.User;

/**
 * @author Nazarpour.
 */
public interface TokenService {

    /**
     * try to generate token if user is authenticated
     * @param appId
     * @param claimedUser
     * @return token as a string
     */
    public String generate(String appId, User claimedUser);

    /**
     * validate token
     * @param token
     * @return corresponding user
     */
    public User validate(String token);


    public void expireToken(String appId, User user, String token);

}