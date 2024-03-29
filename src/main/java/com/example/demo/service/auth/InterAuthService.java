package com.example.demo.service.auth;

import com.example.demo.dto.FirebaseTokenDTO;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("inter")
public class InterAuthService extends AuthService {
    public InterAuthService(UserService userService) {
        super(userService);
        log.info("====================InterAuthService====================");
    }

    @Override
    public FirebaseTokenDTO verifyIdToken(String bearerToken) {
        log.info("====================verifyIdToken====================");
        return new FirebaseTokenDTO("uid-1", "name-1", "admin@gmail.com", "picture-sample");
    }

    @Override
    public void revokeRefreshTokens(String uid) throws FirebaseAuthException {
        log.info("====================revokeRefreshTokens====================");
        log.info("revoke token : {}", uid);
    }
}