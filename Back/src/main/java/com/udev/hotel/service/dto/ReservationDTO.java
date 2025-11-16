package com.udev.hotel.service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ReservationDTO {
	private Long userId;
	private Long chambreId;
	private List<Long> serviceIds;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	private Boolean usePoints;
}
