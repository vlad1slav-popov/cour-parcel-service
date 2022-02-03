package com.api.courierparcelservice.controller;


import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.service.CourService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CourController {

    private final Logger logger;
    private final CourService userService;

    @GetMapping("hello")
    public String hello() {
        logger.info("LOGGER WORKS!");
        return "hello";
    }

    @Secured("ROLE_COURIER")
    @PostMapping("user/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return userService.logout(request);
    }

//    @Secured("ROLE_USER")
//    @GetMapping("users/all")
//    public List<UserEntity> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("user")
//    public ResponseEntity<UserEntity> getUser(@RequestParam String username) {
//        return ResponseEntity.ok(userService.getUserDataByUsername(username));
//    }

}
