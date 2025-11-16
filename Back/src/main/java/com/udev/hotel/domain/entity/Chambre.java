package com.udev.hotel.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udev.hotel.config.constants.EtatChambre;
import com.udev.hotel.config.constants.TypeChambre;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "chambre")
public class Chambre implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "numeroChambre", nullable = false)
	private int numeroChambre;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "etat", nullable = false)
	private EtatChambre etat;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "type", nullable = false)
	private TypeChambre type;

	@NotNull
	@Column(name = "prix", nullable = false)
	private double prix;

	@Column(name = "description", nullable = false)
	private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "chambre_photos", joinColumns = @JoinColumn(name = "chambre_id"))
	@Column(name = "photo_data")
	@Lob
	private List<byte[]> photos = new ArrayList<>();

	public Chambre() {
	}

	public Chambre(@NotNull int numeroChambre, @NotNull EtatChambre etat, @NotNull TypeChambre type,
			@NotNull double prix, String description, List<byte[]> photos) {
		super();
		this.numeroChambre = numeroChambre;
		this.etat = etat;
		this.type = type;
		this.prix = prix;
		this.description = description;
		this.photos = photos;
	}

	public List<byte[]> getPhotos() {
		return photos;
	}

	public void setPhotos(List<byte[]> photos) {
		this.photos = photos;
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

	public EtatChambre getEtat() {
		return etat;
	}

	public void setEtat(EtatChambre etat) {
		this.etat = etat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Chambre [numeroChambre=" + numeroChambre + ", etat=" + etat + ", type=" + type + ", prix=" + prix
				+ ", description=" + description + ", photos=" + photos + "]";
	}
	
	

}
