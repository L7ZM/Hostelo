package com.udev.hotel.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.udev.hotel.domain.entity.ServiceAdditionnel;

public interface ServiceAdditionnelRepository extends JpaRepository<ServiceAdditionnel, Long>{
	List<ServiceAdditionnel> findAll();
	ServiceAdditionnel findByNomService(String nomService);
	
	@Query("""
		    SELECT sa.nomService , r.id
		    FROM ServiceAdditionnel sa
		    JOIN ReservationServiceAdd rsa ON rsa.serviceAdditionnel.id = sa.id
		    JOIN Reservation r ON r.id = rsa.reservation.id
		    JOIN User u ON u.id = r.user.id
		    WHERE u.email = :username
		""")
		List<String> findServicesByUserEmail(@Param("username") String email);
}
