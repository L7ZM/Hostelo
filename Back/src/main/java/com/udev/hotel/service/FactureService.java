package com.udev.hotel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.udev.hotel.config.constants.PaimentStatus;
import com.udev.hotel.domain.entity.Facture;
import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.domain.entity.ReservationServiceAdd;
import com.udev.hotel.domain.repository.FactureRepository;
import com.udev.hotel.domain.repository.ReservationRepository;
import com.udev.hotel.domain.repository.ReservationServiceAddRepository;
import com.udev.hotel.service.dto.FactureDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class FactureService {
	private final FactureRepository factureRepository;
	private final ReservationRepository reservationRepository;
	private final ReservationServiceAddRepository reservationServiceAddRepository;

	public Facture genererFacture(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new IllegalArgumentException("Réservation introuvable."));
	
		double montantTotal = calculerMontantTotal(reservation);

		Facture facture = new Facture();
		facture.setDateEmission(LocalDate.now());
		facture.setMontantTotal(montantTotal);
		facture.setEtatPaiement(PaimentStatus.NOT_PAID);
		facture.setReservation(reservation);

		return factureRepository.save(facture);
	}
	
	public FactureDTO getFactureByReservation(Long reservationId) {
        return factureRepository.getFactureByReservationId(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Facture not found for Reservation ID: " + reservationId));
    }

	public Facture payerFacture(Long factureId) {
		Facture facture = factureRepository.findById(factureId)
				.orElseThrow(() -> new IllegalArgumentException("Facture introuvable."));

		facture.setEtatPaiement(PaimentStatus.PAID);
		return factureRepository.save(facture);
	}

	private double calculerMontantTotal(Reservation reservation) {
		double montantChambre = reservation.getChambre().getPrix();
		double numberOfNights = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
		double pricePerNumberOfNights = (ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin())) * montantChambre;
		Boolean usePoints = reservation.getUsePoints();
		List<ReservationServiceAdd> services = reservationServiceAddRepository.findByReservation(reservation);
		double montantServices = (services.stream().mapToDouble(service -> service.getServiceAdditionnel().getPrix())
				.sum()) * numberOfNights;

		double sumOfReservation = pricePerNumberOfNights + montantServices;
		Log.info(sumOfReservation);
		if(usePoints) {
			return sumOfReservation/2;
		}else {
			
			return sumOfReservation;
		}
	}
	

	public void generateFactureReport(Long factureId, HttpServletResponse response) throws Exception {
		Facture facture = factureRepository.findById(factureId)
				.orElseThrow(() -> new IllegalArgumentException("Facture introuvable."));

		// Prepare data for the report
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("factureId", facture.getId());
		parameters.put("dateEmission", facture.getDateEmission());
		parameters.put("montantTotal", facture.getMontantTotal());
		parameters.put("etatPaiement", facture.getEtatPaiement().toString());
		parameters.put("clientName", facture.getReservation().getUser().getNom());
		parameters.put("roomType", facture.getReservation().getChambre().getType());
		parameters.put("roomPrice", facture.getReservation().getChambre().getPrix());

		List<Map<String, Object>> serviceList = reservationServiceAddRepository.findByReservation(facture.getReservation())
				.stream()
				.map(service -> {
					Map<String, Object> serviceData = new HashMap<>();
					serviceData.put("serviceName", service.getServiceAdditionnel().getNomService());
					serviceData.put("servicePrice", service.getServiceAdditionnel().getPrix());
					return serviceData;
				}).toList();

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(serviceList);

		// Load the Jasper template
		ClassPathResource resource = new ClassPathResource("reports/facture_template.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), parameters, dataSource);

		// Export to PDF
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=facture_" + factureId + ".pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}
	
	
}
