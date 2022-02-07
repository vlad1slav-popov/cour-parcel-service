package com.api.courierparcelservice.service;


import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.domain.LogoutResponse;
import com.api.courierparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.RoleEntity;
import com.api.courierparcelservice.exception.UserException;
import com.api.courierparcelservice.repository.CourRepository;
import com.api.courierparcelservice.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourService {

    private final CourRepository courRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<CourEntity> getAllCour() {
        RoleEntity role_cour = roleRepository.findRoleByName("ROLE_COURIER");

        return courRepository.findAllByRoles(role_cour);
    }

    public CourEntity getUserDataByUsername(String username) {
        return Optional.ofNullable(courRepository.findUserEntityByUsername(username))
                .orElseThrow(() -> new UserException("Courier with username: " +
                        username + " not found", "005"));
    }

    public CourEntity getUserDataByUsernameAndPassword(CourLoginRequest courLoginRequest) {
        CourEntity userEntity = Optional.ofNullable(courRepository
                        .findUserEntityByUsername(courLoginRequest.getUsername()))
                .orElseThrow(() -> new UserException("Courier with username: " +
                        courLoginRequest.getUsername() + " not found", "005"));

        if (passwordEncoder.matches(courLoginRequest.getPassword(), userEntity.getPassword())) {
            return userEntity;
        } else
            throw new UserException("Wrong password", "006");
    }

    public LogoutResponse logout(LogoutRequest request) {
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(
                request.getUsername(), request.getToken(), request);
//        logger.info("OnUserLogoutSuccessEvent: " + logoutSuccessEvent);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return LogoutResponse.builder()
                        .message("User has successfully logged out from the system!")
                .build();
    }


}
