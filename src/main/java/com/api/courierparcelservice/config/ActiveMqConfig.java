package com.api.courierparcelservice.config;


import com.api.courierparcelservice.domain.CourRegisterRequest;
import com.api.courierparcelservice.domain.CourLoginRequest;
import com.api.courierparcelservice.domain.LogoutResponse;
import com.api.courierparcelservice.dto.MqDTO;
import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.RoleEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ActiveMqConfig {

    @Bean
    MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("content-type");

        Map<String, Class<?>> typeIdMapping = new HashMap<>();
        typeIdMapping.put("courEntity", CourEntity.class);
        typeIdMapping.put("courLoginRequest", CourLoginRequest.class);
        typeIdMapping.put("roles", RoleEntity.class);
        typeIdMapping.put("mqDTO", MqDTO.class);
        typeIdMapping.put("courRegisterRequest", CourRegisterRequest.class);
        typeIdMapping.put("logoutResponse", LogoutResponse.class);
        converter.setTypeIdMappings(typeIdMapping);
        return converter;
    }

//    @Bean
//    JmsTemplate jmsTemplate() throws JMSException {
//        JmsTemplate jmsTemplate = new JmsTemplate();
////        jmsTemplate.setDefaultDestinationName("testqueue");
////        ConnectionFactory connectionFactory = new TargetConn();
////        connectionFactory.createConnection();
////        jmsTemplate.setConnectionFactory(connectionFactory);
//        return jmsTemplate;
//    }
}
