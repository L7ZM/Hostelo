package com.udev.hotel.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.udev.hotel.config.constants.ReservationStatus;
import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.service.dto.ReservationRequest;
import com.udev.hotel.service.dto.ReservationResponse;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByStatus(@Param("status") ReservationStatus status);

	@Query("SELECT r FROM Reservation r WHERE r.chambre.id = :chambreId AND "
			+ "(r.dateDebut < :dateFin AND r.dateFin > :dateDebut)")
	List<Reservation> findByChambreAndDateOverlap(@Param("chambreId") Long chambreId,
			@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

	@Query("SELECT r FROM Reservation r WHERE r.user.email = :username")
	List<Reservation> getReservationsByUsername(@Param("username") String username);

	@Query(value = """
		    SELECT r.id AS id,
		           r.user_id AS idUser,
		           c.numero_chambre AS numeroChambre,
		           u.last_name AS nom,
		           u.first_name AS prenom,
		           r.date_debut AS dateDebut,
		           r.date_fin AS dateFin,
		           r.status AS status,
		           r.use_points AS usePoints,
		           COALESCE(ARRAY_AGG(sa.nom_service || ' (' || sa.prix || 'MAD)'), '{}') AS services
		    FROM reservation r
		    LEFT JOIN chambre c ON r.chambre_id = c.id
		    LEFT JOIN users u ON r.user_id = u.id
		    LEFT JOIN reservation_service_add rsa ON rsa.reservation_id = r.id
		    LEFT JOIN service_additionnel sa ON rsa.service_additionnel_id = sa.id
		    WHERE u.email = :username
		    GROUP BY r.id, c.numero_chambre, u.last_name, u.first_name, r.date_debut, r.date_fin, r.status ,r.use_points
		""", nativeQuery = true)
		List<Object[]> ReservationsByUsername(@Param("username") String username);
	
	@Query(value = """
		    SELECT r.id AS id,
		           r.user_id AS idUser,
		           c.numero_chambre AS numeroChambre,
		           u.last_name AS nom,
		           u.first_name AS prenom,
		           r.date_debut AS dateDebut,
		           r.date_fin AS dateFin,
		           r.status AS status,
		           r.use_points AS usePoints,
		           COALESCE(ARRAY_AGG(sa.nom_service || ' (' || sa.prix || 'MAD)'), '{}') AS services
		    FROM reservation r
		    LEFT JOIN chambre c ON r.chambre_id = c.id
		    LEFT JOIN users u ON r.user_id = u.id
		    LEFT JOIN reservation_service_add rsa ON rsa.reservation_id = r.id
		    LEFT JOIN service_additionnel sa ON rsa.service_additionnel_id = sa.id
		    GROUP BY r.id, c.numero_chambre, u.last_name, u.first_name, r.date_debut, r.date_fin, r.status, r.use_points
		""", nativeQuery = true)
		List<Object[]> getAllReservationsWithServices();


}
