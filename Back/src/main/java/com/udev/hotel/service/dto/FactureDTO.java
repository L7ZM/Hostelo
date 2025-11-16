package com.udev.hotel.service.dto;

import java.time.LocalDate;

import com.udev.hotel.config.constants.PaimentStatus;

import lombok.Data;

@Data
public class FactureDTO {
    private Long id;
    private LocalDate dateEmission;
    private double montantTotal;
    private PaimentStatus etatPaiement;

    public FactureDTO(Long id, LocalDate dateEmission, double montantTotal, PaimentStatus etatPaiement  ) {
        this.id = id;
        this.dateEmission = dateEmission;
        this.montantTotal = montantTotal;
        this.etatPaiement = etatPaiement;
    }
}