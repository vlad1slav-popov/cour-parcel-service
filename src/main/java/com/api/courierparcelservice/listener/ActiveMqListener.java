package com.api.courierparcelservice.listener;

import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.domain.CourRegisterRequest;
import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.LogoutResponse;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.exception.UserException;
import com.api.courierparcelservice.service.AuthenticationService;
import com.api.courierparcelservice.service.CourAuthorizationService;
import com.api.courierparcelservice.service.CourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

    @JmsListener(destination = "cour-login-req-queue")
    public void getLoginValue(CourLoginRequest courLoginRequest) {
        log.info("message received: " + courLoginRequest);
        try {
            MqDTO loginResponse = authenticationService.getLoginResponse(courLoginRequest);
            jmsTemplate.convertAndSend("cour-login-res-queue", loginResponse);
        } catch (UserException e) {
            MqDTO loginResponse = new MqDTO();
            CourEntity entity = new CourEntity();
            entity.setErrCode(e.getCode());
            entity.setErrDescription(e.getMessage());
            loginResponse.setCourEntity(entity);
            jmsTemplate.convertAndSend("cour-login-res-queue", loginResponse);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            MqDTO loginResponse = new MqDTO();
            CourEntity entity = new CourEntity();
            entity.setErrCode("100");
            entity.setErrDescription("invalidusername of password");
            loginResponse.setCourEntity(entity);
            jmsTemplate.convertAndSend("cour-login-res-queue", loginResponse);
        }
    }

    @JmsListener(destination = "cour-register-req-queue")
    public void getRegisterValue(CourRegisterRequest courRegisterRequest) {
        log.info("message received: " + courRegisterRequest);
        try {
            CourEntity courEntity = authorizationService
                    .getRegisterResponse(courRegisterRequest);
            jmsTemplate.convertAndSend("cour-register-res-queue", courEntity);
        } catch (UserException e) {
            CourEntity courEntity = new CourEntity();
            courEntity.setErrCode(e.getCode());
            courEntity.setErrDescription(e.getMessage());
            jmsTemplate.convertAndSend("cour-register-res-queue", courEntity);
        }

    }

    @JmsListener(destination = "cour-getallcour-req-queue")
    public void getAllCour(String message) {
        log.info("message received: " + message);
        List<CourEntity> allCour = courService.getAllCour();
        jmsTemplate.convertAndSend("cour-getallcour-res-queue", allCour);
    }

    @JmsListener(destination = "cour-logout-req-queue")
    public void logout(LogoutRequest request) {
        log.info("message received: " + request);
        LogoutResponse logoutResponse = courService.logout(request);
        jmsTemplate.convertAndSend("cour-logout-res-queue", logoutResponse);
    }


}
