package com.udev.hotel.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "serviceAdditionnel")
public class ServiceAdditionnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
	@Column(name = "nomService", nullable = false)
    private String nomService;

    @NotNull
	@Column(name = "description", nullable = false)
	private String description;
    
	@NotNull
	@Column(name = "prix", nullable = false)
	private double prix;

    public ServiceAdditionnel() {
    	
    }

	public ServiceAdditionnel(Long id, String nomService, String description, double prix) {
		super();
		this.id = id;
		this.nomService = nomService;
		this.description = description;
		this.prix = prix;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomService() {
		return nomService;
	}

	public void setNomService(String nomService) {
		this.nomService = nomService;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}
    
    
}
