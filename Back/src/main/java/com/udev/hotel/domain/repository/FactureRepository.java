package com.udev.hotel.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.udev.hotel.domain.entity.Facture;
import com.udev.hotel.service.dto.FactureDTO;

public interface FactureRepository extends JpaRepository<Facture, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM Facture f WHERE f.reservation.id = :idReservation")
	void deleteFactureByIdReservation(@Param("idReservation") Long idReservation);

	@Query("SELECT new com.udev.hotel.service.dto.FactureDTO(f.id, f.dateEmission, f.montantTotal, f.etatPaiement) FROM Facture f WHERE f.reservation.id = :idReservation")
	Optional<FactureDTO> getFactureByReservationId(@Param("idReservation") Long idReservation);
}
