package com.api.courierparcelservice.service;



import com.api.courierparcelservice.domain.CourRegisterRequest;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.RoleEntity;
import com.api.courierparcelservice.entity.Status;
import com.api.courierparcelservice.exception.UserException;
import com.api.courierparcelservice.repository.CourRepository;
import com.api.courierparcelservice.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class CourAuthorizationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CourRepository userLoginRepository;

    private final RoleRepository roleRepository;

    public CourAuthorizationService(BCryptPasswordEncoder bCryptPasswordEncoder,
                                    CourRepository userLoginRepository,
                                    RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
    }


    public CourEntity getRegisterResponse(CourRegisterRequest request) {

        if (Objects.isNull(request.getPassword()) ||
                Objects.isNull(request.getUsername())) {
            throw new UserException("Username or password are empty", "002");
        }

        if (request.getPassword().trim().isEmpty() ||
                request.getUsername().trim().isEmpty()) {
            throw new UserException("password or username is empty", "003");
        }

        if (Objects.nonNull(userLoginRepository.findUserEntityByUsername(request.getUsername()))) {
            throw new UserException("Username already exists", "004");
        }

        String encodedPass = bCryptPasswordEncoder.encode(request.getPassword());
        RoleEntity roleCourier = roleRepository.findRoleByName("ROLE_COURIER");
        List<RoleEntity> roleEntityList = new ArrayList<>();
        roleEntityList.add(roleCourier);



        CourEntity userEntity = CourEntity.builder()
                .username(request.getUsername())
                .password(encodedPass)
                .roles(roleEntityList)
                .status(Status.ACTIVE)
                .build();
        userLoginRepository.save(userEntity);

        return userEntity;

    }


}
