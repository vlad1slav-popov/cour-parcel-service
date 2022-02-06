package com.api.courierparcelservice.dto;

import com.api.courierparcelservice.entity.CourEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MqDTO {

    private String token;
    private CourEntity courEntity;
}
