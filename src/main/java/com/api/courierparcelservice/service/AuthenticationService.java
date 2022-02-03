package com.api.courierparcelservice.service;




import com.api.courierparcelservice.domain.UserLoginRequest;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.Status;
import com.api.courierparcelservice.exception.UserException;
import com.api.courierparcelservice.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final CourService courService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 CourService courService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.courService = courService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<CourEntity> getLoginResponse(UserLoginRequest requestDto) {
        System.out.println("ok");
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
//            System.out.println(token);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer_" + token);

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(user);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
