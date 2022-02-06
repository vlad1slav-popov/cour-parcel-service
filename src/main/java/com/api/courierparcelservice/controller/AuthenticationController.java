package com.api.courierparcelservice.controller;





import com.api.courierparcelservice.domain.RegisterUserRequest;
import com.api.courierparcelservice.domain.UserLoginRequest;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.service.AuthenticationService;
import com.api.courierparcelservice.service.CourAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/cour/login")
    public ResponseEntity<CourEntity> login(@RequestBody UserLoginRequest requestDto) {
        MqDTO loginResponse = authenticationService.getLoginResponse(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + loginResponse.getToken());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(loginResponse.getCourEntity());
    }

}
