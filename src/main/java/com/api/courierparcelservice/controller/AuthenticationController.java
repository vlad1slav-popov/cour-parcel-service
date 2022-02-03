package com.api.courierparcelservice.controller;





import com.api.courierparcelservice.domain.RegisterUserRequest;
import com.api.courierparcelservice.domain.UserLoginRequest;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.service.AuthenticationService;
import com.api.courierparcelservice.service.CourAuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CourAuthorizationService userAuthorizationService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    CourAuthorizationService userAuthorizationService) {
        this.authenticationService = authenticationService;
        this.userAuthorizationService = userAuthorizationService;
    }


    @PostMapping("/login")
    public ResponseEntity<CourEntity> login(@RequestBody UserLoginRequest requestDto) {
        return authenticationService.getLoginResponse(requestDto);
    }


    @PostMapping("/register")
    public ResponseEntity<CourEntity> register(@RequestBody RegisterUserRequest request) {
        return userAuthorizationService.getRegisterResponse(request);
    }

}
