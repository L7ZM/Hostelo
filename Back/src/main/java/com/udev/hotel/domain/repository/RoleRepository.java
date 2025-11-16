package com.udev.hotel.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udev.hotel.domain.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

    Optional<Role> findByName(String roleName);
}
