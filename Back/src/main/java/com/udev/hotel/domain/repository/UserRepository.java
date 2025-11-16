package com.udev.hotel.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.udev.hotel.domain.entity.User;
import com.udev.hotel.service.dto.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Query("SELECT new com.udev.hotel.service.dto.UserDTO(u) FROM User u WHERE u.id = :idUser")
	UserDTO findUserById(@Param("idUser") Long idUser);
	
    Optional<User> findOneByEmailIgnoreCase(String email);
    
    Optional<User> findByNom(String nom);
}