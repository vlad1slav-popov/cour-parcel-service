package com.api.courierparcelservice.repository;


import com.api.courierparcelservice.entity.CourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourRepository extends JpaRepository<CourEntity, Long> {


    CourEntity findUserEntityByUsername(String username);

    CourEntity findUserEntityByUsernameAndPassword(String username, String password);


}

