package com.udev.hotel.config;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginResponse {
	private String token;

	private long expiresIn;

	private Long id;
	private String username;
	private String nom;
	private String prenom;
	private String email;
	private LocalDate dateNaissance;
	private String adresse;
	private String telephone;
	private List<String> roles;
	private int pointsFidelite;
	

	public Long getId() {
		return id;
	}

	public LoginResponse setId(Long id) {
		this.id = id;
		return this;
	}
	public String getToken() {
		return token;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public LoginResponse setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}

	public LoginResponse setToken(String token) {
		this.token = token;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public LoginResponse setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public LoginResponse setEmail(String email) {
		this.email = email;
		return this;
	}

	public List<String> getRoles() {
		return roles;
	}

	public LoginResponse setRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public LoginResponse setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
		return this;

	}

	public String getAdresse() {
		return adresse;
	}

	public LoginResponse setAdresse(String adresse) {
		this.adresse = adresse;
		return this;
	}

	public String getTelephone() {
		return telephone;
	}

	public LoginResponse setTelephone(String telephone) {
		this.telephone = telephone;
		return this;
	}

	public String getNom() {
		return nom;
	}

	public LoginResponse setNom(String nom) {
		this.nom = nom;
		return this;
	}

	public String getPrenom() {
		return prenom;
	}

	public LoginResponse setPrenom(String prenom) {
		this.prenom = prenom;
		return this;
	}

	public int getPointsFidelite() {
		return pointsFidelite;
	}

	public LoginResponse setPointsFidelite(int pointsFidelite) {
		this.pointsFidelite = pointsFidelite;
		return this;
	}
	
	

}