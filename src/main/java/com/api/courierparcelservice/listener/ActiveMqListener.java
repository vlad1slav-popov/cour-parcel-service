package com.api.courierparcelservice.listener;

import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.domain.CourRegisterRequest;
import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.LogoutResponse;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.service.AuthenticationService;
import com.api.courierparcelservice.service.CourAuthorizationService;
import com.api.courierparcelservice.service.CourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActiveMqListener {

    private final JmsTemplate jmsTemplate;
    private final AuthenticationService authenticationService;
    private final CourAuthorizationService authorizationService;
    private final CourService courService;

    @JmsListener(destination = "requestqueue")
    public void getLoginValue(CourLoginRequest courLoginRequest) {
        log.info("message received: " + courLoginRequest);

        MqDTO loginResponse = authenticationService.getLoginResponse(courLoginRequest);
        jmsTemplate.convertAndSend("responsequeue", loginResponse);
    }

    @JmsListener(destination = "requestqueue")
    public void getRegisterValue(CourRegisterRequest courRegisterRequest) {
        log.info("message received: " + courRegisterRequest);

        CourEntity courEntity = authorizationService
                .getRegisterResponse(courRegisterRequest);

        jmsTemplate.convertAndSend("responsequeue", courEntity);
    }

    @JmsListener(destination = "requestqueue")
    public void getAllCour(String message) {
        log.info("message received: " + message);
        List<CourEntity> allCour = courService.getAllCour();
        jmsTemplate.convertAndSend("responsequeue", allCour);
    }

    @JmsListener(destination = "requestqueue")
    public void logout(LogoutRequest request) {
        log.info("message received: " + request);
        LogoutResponse logoutResponse = courService.logout(request);
        jmsTemplate.convertAndSend("responsequeue", logoutResponse);
    }


}
