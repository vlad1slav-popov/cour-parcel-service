package com.api.courierparcelservice.service;


import com.api.courierparcelservice.domain.LogoutRequest;
import com.api.courierparcelservice.domain.UserLoginRequest;
import com.api.courierparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.exception.UserNotFoundException;
import com.api.courierparcelservice.repository.CourRepository;
import com.api.courierparcelservice.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
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

    public List<CourEntity> getAllUsers() {
        return courRepository.findAll();
    }

    public CourEntity getUserDataByUsername(String username) {
        return Optional.ofNullable(courRepository.findUserEntityByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
    }

    public CourEntity getUserDataByUsernameAndPassword(UserLoginRequest userLoginRequest) {
        CourEntity userEntity = Optional.ofNullable(courRepository
                        .findUserEntityByUsername(userLoginRequest.getUsername()))
                .orElseThrow(() -> new UserNotFoundException("Courier with username: " +
                        userLoginRequest.getUsername() + " not found"));

        if (passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword())) {
            return userEntity;
        } else
            throw new UserNotFoundException("Wrong password");
    }

    public ResponseEntity<String> logout(LogoutRequest request) {
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(
                request.getUsername(), request.getToken(), request);
//        logger.info("OnUserLogoutSuccessEvent: " + logoutSuccessEvent);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok("User has successfully logged out from the system!");
    }


}
