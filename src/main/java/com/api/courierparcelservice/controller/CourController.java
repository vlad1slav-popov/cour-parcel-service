package com.api.courierparcelservice.controller;


import com.api.courierparcelservice.domain.CourRegisterRequest;
import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.LogoutResponse;
import com.api.courierparcelservice.entity.CourEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CourController {

   private final JmsTemplate jmsTemplate;

    @Secured("ROLE_COURIER, ROLE_ADMIN")
    @PostMapping("cour/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest logoutRequest) {
        jmsTemplate.convertAndSend("cour-logout-req-queue", logoutRequest);
        return ResponseEntity.ok((LogoutResponse) jmsTemplate
                .receiveAndConvert("cour-logout-res-queue"));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("cour/register")
    public ResponseEntity<CourEntity> register(@RequestBody CourRegisterRequest courRegisterRequest) {

        jmsTemplate.convertAndSend("cour-register-req-queue", courRegisterRequest);
        return ResponseEntity.ok((CourEntity) jmsTemplate
                .receiveAndConvert("cour-register-res-queue"));

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/cour/all")
    public ResponseEntity<List<CourEntity>> findAllCour() {
        jmsTemplate.convertAndSend("cour-findallcour-req-queue", "/cour/all");
        return ResponseEntity.ok((List<CourEntity>) jmsTemplate
                .receiveAndConvert("cour-findallcour-res-queue"));
    }

}
