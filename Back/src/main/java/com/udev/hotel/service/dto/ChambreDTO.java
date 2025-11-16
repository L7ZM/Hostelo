package com.udev.hotel.service.dto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import com.udev.hotel.config.constants.EtatChambre;
import com.udev.hotel.config.constants.TypeChambre;
import com.udev.hotel.domain.entity.Chambre;

public class ChambreDTO {
    private Long id;
    private int numeroChambre;
    private EtatChambre etat;
    private TypeChambre type;
    private double prix;
    private String description;
    private List<String> photos;

    public ChambreDTO(Chambre chambre) {
        this.id = chambre.getId();
        this.numeroChambre = chambre.getNumeroChambre();
        this.etat = chambre.getEtat();
        this.type = chambre.getType();
        this.prix = chambre.getPrix();
        this.description = chambre.getDescription();
        this.photos = chambre.getPhotos().stream()
                             .map(photo -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo))
                             .collect(Collectors.toList());
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumeroChambre() {
		return numeroChambre;
	}

	public void setNumeroChambre(int numeroChambre) {
		this.numeroChambre = numeroChambre;
	}

	public EtatChambre getEtat() {
		return etat;
	}

	public void setEtat(EtatChambre etat) {
		this.etat = etat;
	}

	public TypeChambre getType() {
		return type;
	}

	public void setType(TypeChambre type) {
		this.type = type;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

    
   
}
