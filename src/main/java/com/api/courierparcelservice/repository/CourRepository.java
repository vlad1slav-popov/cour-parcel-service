package com.api.courierparcelservice.repository;


import com.api.courierparcelservice.entity.CourEntity;
import com.api.courierparcelservice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourRepository extends JpaRepository<CourEntity, Long> {


    CourEntity findUserEntityByUsername(String username);

    CourEntity findUserEntityByUsernameAndPassword(String username, String password);

    List<CourEntity> findAllByRoles(RoleEntity roleEntity);



}

