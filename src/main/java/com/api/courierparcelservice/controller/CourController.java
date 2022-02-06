package com.api.courierparcelservice.controller;


import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.RegisterUserRequest;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.service.CourAuthorizationService;
import com.api.courierparcelservice.service.CourService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CourController {

    private final Logger logger;
    private final CourService userService;
    private final CourAuthorizationService authorizationService;

    @GetMapping("hello")
    public String hello() {
        logger.info("LOGGER WORKS!");
        return "hello";
    }

    @Secured("ROLE_COURIER, ROLE_ADMIN")
    @PostMapping("cour/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return userService.logout(request);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("cour/register")
    public ResponseEntity<CourEntity> register(@RequestBody RegisterUserRequest request) {
        CourEntity registerResponse = authorizationService.getRegisterResponse(request);
        return ResponseEntity.ok()
                .body(registerResponse);
    }

}
