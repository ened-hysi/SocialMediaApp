package com.example.social_media_app.controller;

import com.example.social_media_app.security.CustomUserDetails;
import com.example.social_media_app.security.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LogInController {

    @Autowired
    private UserDetailService userDetailService;


    private final AuthenticationManager authenticationManager;

    public LogInController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody String jsonBody, HttpServletRequest req) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> loginCredentials = mapper.readValue(jsonBody, Map.class);
            String username = loginCredentials.get("username");
            String password = loginCredentials.get("password");

            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            String sessionId = session.getId();


            CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(username);
            String userRole = userDetails.getUserRole();

            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", sessionId);
            response.put("userRole", userRole);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
