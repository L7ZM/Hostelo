package com.udev.hotel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udev.hotel.config.constants.ReservationStatus;
import com.udev.hotel.domain.entity.Chambre;
import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.domain.entity.ReservationServiceAdd;
import com.udev.hotel.domain.entity.ServiceAdditionnel;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.domain.repository.ChambreRepository;
import com.udev.hotel.domain.repository.FactureRepository;
import com.udev.hotel.domain.repository.ReservationRepository;
import com.udev.hotel.domain.repository.ReservationServiceAddRepository;
import com.udev.hotel.domain.repository.ServiceAdditionnelRepository;
import com.udev.hotel.domain.repository.UserRepository;
import com.udev.hotel.service.dto.ReservationRequest;
import com.udev.hotel.service.dto.ReservationResponse;

import io.jsonwebtoken.lang.Arrays;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	
	private final Logger log = LoggerFactory.getLogger(ReservationService.class);
	private final ReservationRepository reservationRepository;
	private final ChambreRepository chambreRepository;
	private final UserRepository userRepository;
	private final ServiceAdditionnelRepository serviceAdditionnelRepository;
	private final ReservationServiceAddRepository reservationServiceAddRepository;
	@Autowired
	private FactureRepository factureRepository;

	@Transactional
	public Reservation createReservation(String username, Long chambreId, List<Long> serviceIds, LocalDate dateDebut,
			LocalDate dateFin , Boolean usePoints) {
		Chambre chambre = chambreRepository.findById(chambreId)
				.orElseThrow(() -> new IllegalArgumentException("La chambre spécifiée est introuvable."));

		boolean isDisponible = reservationRepository.findByChambreAndDateOverlap(chambreId, dateDebut, dateFin)
				.isEmpty();
		if (!isDisponible) {
			throw new IllegalArgumentException("La chambre n'est pas disponible pour les dates spécifiées.");
		}

		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
		if(user != null) {
			int currentPoints = user.getPointsFidelite();
			user.setPointsFidelite(currentPoints +10);
		} 
		if(usePoints) {
			int currentPoints = user.getPointsFidelite();
			user.setPointsFidelite(currentPoints - 50);
		}
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setChambre(chambre);
		reservation.setDateDebut(dateDebut);
		reservation.setDateFin(dateFin);
		reservation.setStatus(ReservationStatus.EN_ATTENTE);
		reservation.setUsePoints(usePoints);
		Reservation savedReservation = reservationRepository.save(reservation);

		if (serviceIds != null && !serviceIds.isEmpty()) {
			List<ServiceAdditionnel> services = serviceAdditionnelRepository.findAllById(serviceIds);
			for (ServiceAdditionnel service : services) {
				ReservationServiceAdd reservationServiceAdd = new ReservationServiceAdd();
				reservationServiceAdd.setReservation(savedReservation);
				reservationServiceAdd.setServiceAdditionnel(service);
				reservationServiceAddRepository.save(reservationServiceAdd);
			}
		}

		return savedReservation;
	}

	public void cancelReservation(Long reservationId) {
    	String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    	User user = userRepository.findByEmail(currentUsername)
				.orElseThrow(() -> new IllegalArgumentException("User introuvable."));
    	int currentPoints = user.getPointsFidelite(); 
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new IllegalArgumentException("Reservation introuvable."));

		LocalDate currentDate = LocalDate.now();
		LocalDate dateDebutMinus48Hours = reservation.getDateDebut().minusDays(2);

		if (currentDate.isAfter(dateDebutMinus48Hours)) {
			log.info("Cancellation not allowed within 48 hours of the reservation start date, if you continue you're fidelity point decreased by 5 points");
			
	        user.setPointsFidelite(currentPoints - 5); 
	        userRepository.save(user);
		}
		user.setPointsFidelite(currentPoints - 10);
        factureRepository.deleteFactureByIdReservation(reservationId);
		reservationRepository.delete(reservation);
	}
	
	public void cancelReservationByAdmin(Long reservationId , String username) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new IllegalArgumentException("Reservation introuvable."));
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new IllegalArgumentException("Reservation introuvable."));
		int currentPoints = user.getPointsFidelite(); 
		LocalDate currentDate = LocalDate.now();
		LocalDate dateDebutMinus48Hours = reservation.getDateDebut().minusDays(2);

		if (currentDate.isAfter(dateDebutMinus48Hours)) {
			log.info("Cancellation not allowed within 48 hours of the reservation start date, if you continue you're fielity point decreased by 10points");

	        user.setPointsFidelite(currentPoints - 5); 
	        userRepository.save(user);
		}
		user.setPointsFidelite(currentPoints - 10);
        factureRepository.deleteFactureByIdReservation(reservationId);
		reservationRepository.delete(reservation);
	}

//	public List<ReservationResponse> getAllreservation() {
//		return reservationRepository.getAllreservation();
//	}
	
//	--------------------
	public List<ReservationResponse> getAllReservations() {
	    List<Object[]> results = reservationRepository.getAllReservationsWithServices();
	    List<ReservationResponse> reservations = new ArrayList<>();

	    for (Object[] row : results) {
	        Long id = row[0] != null ? ((Number) row[0]).longValue() : null;
	        Long idUser = row[1] != null ? ((Number) row[1]).longValue() : null;
	        int numeroChambre = row[2] != null ? (Integer) row[2] : 0; // Assign 0 if null
	        String nom = row[3] != null ? (String) row[3] : "Unknown"; // Default to "Unknown"
	        String prenom = row[4] != null ? (String) row[4] : "Unknown";
	        LocalDate dateDebut = row[5] != null ? LocalDate.parse(row[5].toString()) : null;
	        LocalDate dateFin = row[6] != null ? LocalDate.parse(row[6].toString()) : null;
	        ReservationStatus status = row[7] != null ? ReservationStatus.valueOf((String) row[7]) : null;
	        boolean usePoints = row[8] != null && (Boolean) row[8];
	        List<String> services;
	        if (row[9] != null && row[9] instanceof String[]) {
	            String[] serviceArray = (String[]) row[9];
	            services = serviceArray.length > 0 ? List.of(serviceArray) : List.of("pas de service add");
	        } else {
	            services = List.of("pas de service add");
	        }
	        reservations.add(
	            new ReservationResponse(id, idUser, numeroChambre, nom, prenom, dateDebut, dateFin, status,usePoints, services)
	        );
	    }

	    return reservations;
	}
	
	@Transactional
	public List<ReservationResponse> getReservationsByUsername(String username) {
		  List<Object[]> results = reservationRepository.ReservationsByUsername(username);
		    List<ReservationResponse> reservations = new ArrayList<>();

		    for (Object[] row : results) {
		        Long id = row[0] != null ? ((Number) row[0]).longValue() : null;
		        Long idUser = row[1] != null ? ((Number) row[1]).longValue() : null;
		        int numeroChambre = row[2] != null ? (Integer) row[2] : 0; 
		        String nom = row[3] != null ? (String) row[3] : "Unknown"; 
		        String prenom = row[4] != null ? (String) row[4] : "Unknown";
		        LocalDate dateDebut = row[5] != null ? LocalDate.parse(row[5].toString()) : null;
		        LocalDate dateFin = row[6] != null ? LocalDate.parse(row[6].toString()) : null;
		        ReservationStatus status = row[7] != null ? ReservationStatus.valueOf((String) row[7]) : null;
		        boolean usePoints = row[8] != null && (Boolean) row[8];
		        
		        List<String> services;
		        if (row[9] != null && row[9] instanceof String[]) {
		            String[] serviceArray = (String[]) row[9];
		            services = serviceArray.length > 0 ? List.of(serviceArray) : List.of("pas de service add");
		        } else {
		            services = List.of("pas de service add");
		        }
		        reservations.add(
		            new ReservationResponse(id, idUser, numeroChambre, nom, prenom, dateDebut, dateFin, status,usePoints, services)
		        );
		    }

		    return reservations;

	}
	
	 public List<String> getServicesByUserEmail(String email) {
	        
		 return serviceAdditionnelRepository.findServicesByUserEmail(email);
	        		
	    }
	 
	 
	
	public void validateReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.EN_ATTENTE) {
            reservation.setStatus(ReservationStatus.CONFIRMEE);
            reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("Reservation cannot be confirmed in its current status.");
        }
    }
}