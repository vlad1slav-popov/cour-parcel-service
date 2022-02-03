package com.api.courierparcelservice.security;




import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.exception.UserNotFoundException;
import com.api.courierparcelservice.security.jwt.JwtUser;
import com.api.courierparcelservice.security.jwt.JwtUserFactory;
import com.api.courierparcelservice.service.CourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final CourService courService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CourEntity userEntity = Optional.ofNullable(courService.getUserDataByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        JwtUser jwtUser = JwtUserFactory.create(userEntity);

        log.info("IN loadByUserName jwt user: " + jwtUser);
        return jwtUser;
    }
}
