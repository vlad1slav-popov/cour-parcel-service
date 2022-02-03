package com.api.courierparcelservice.domain;

import lombok.Data;

@Data
public class LogoutRequest {

    private String token;
    private String username;
}
