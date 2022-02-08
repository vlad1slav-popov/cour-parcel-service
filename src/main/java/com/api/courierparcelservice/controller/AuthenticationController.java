package com.api.courierparcelservice.controller;


import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JmsTemplate jmsTemplate;

    @PostMapping("/cour/login")
    public ResponseEntity<CourEntity> login(@RequestBody CourLoginRequest courLoginRequest) {
        jmsTemplate.convertAndSend("cour-login-req-queue", courLoginRequest);

        MqDTO mqDTO = (MqDTO) jmsTemplate
                .receiveAndConvert("cour-login-resp-queue");

        CourEntity courEntity = mqDTO.getCourEntity();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + mqDTO.getToken());


        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(courEntity);
    }

}
