package com.nikolas.taskflow.auth.adapters.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("auth/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                Map.of(
                        "sub", jwt.getSubject(),
                        "preferred_username", jwt.getClaimAsString("preferred_username"),
                        "email", jwt.getClaimAsString("email"),
                        "realm_access", jwt.getClaim("realm_access"),
                        "resource_access", jwt.getClaim("resource_access")
                )
        );
    }
}
