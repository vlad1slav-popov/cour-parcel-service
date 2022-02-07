package com.api.courierparcelservice.service;




import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.Status;
import com.api.courierparcelservice.exception.UserException;
import com.api.courierparcelservice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CourService courService;


    public MqDTO getLoginResponse(CourLoginRequest requestDto) {
        try {
            String username = requestDto.getUsername();
            CourEntity user = courService.getUserDataByUsernameAndPassword(requestDto);
            System.out.println(user);

            if (!user.getStatus().equals(Status.ACTIVE)) {
                throw new UserException("COUR STATUS IS: " + user.getStatus());
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                   requestDto.getPassword()));
            System.out.println("authenticated");

            String token = jwtTokenProvider.createToken(user, user.getRoles());
            return MqDTO.builder()
                    .token(token)
                    .courEntity(user)
                    .build();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
