package com.udev.hotel.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udev.hotel.config.security.AuthoritiesConstants;
import com.udev.hotel.config.security.SecurityUtils;
import com.udev.hotel.controller.exceptionHandler.EmailAlreadyUsedException;
import com.udev.hotel.domain.entity.Role;
import com.udev.hotel.domain.entity.ServiceAdditionnel;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.domain.repository.RoleRepository;
import com.udev.hotel.domain.repository.ServiceAdditionnelRepository;
import com.udev.hotel.domain.repository.UserRepository;
import com.udev.hotel.service.dto.UserDTO;

@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private ServiceAdditionnelRepository serviceAddRepo;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerUser(UserDTO userDTO, String password) {

		User newUser = new User();
		Role authority = roleRepository.findByName(AuthoritiesConstants.USER)
				.orElseThrow(() -> new IllegalArgumentException("Role not found: " + AuthoritiesConstants.USER));

		Set<Role> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setEmail(userDTO.getEmail());
		newUser.setPassword(encryptedPassword);
		newUser.setPrenom(userDTO.getPrenom());
		newUser.setNom(userDTO.getNom());
		newUser.setAdresse(userDTO.getAdresse());
		newUser.setDateNaissance(userDTO.getDateNaissance());
		newUser.setTelephone(userDTO.getTelephone());
		newUser.setEmail(userDTO.getEmail());
		authorities.add(authority);
		newUser.setRoles(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public Optional<User> loadUserByUsername(String username) {
		return userRepository.findByEmail(username);

	}

	/**
	 * Update basic information (first name, last name, email ..) for the current
	 * user.
	 *
	 * @param prenom        first name of user
	 * @param nom           last name of user
	 * @param email         email id of user
	 * @param adresse       language key
	 * @param telephone     of user
	 * @param dateNaissance of user
	 * 
	 */
	public void updateUser(String prenom, String nom, String email, String password, String adresse, String telephone,
			LocalDate dateNaissance) {

		SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findByEmail).ifPresent(user -> {
			if (prenom != null) {
				user.setPrenom(prenom);
			}
			if (nom != null) {
				user.setNom(nom);
			}
			if (email != null) {
				Optional<User> existingUserByEmail = userRepository.findByEmail(email);
				if (existingUserByEmail.isPresent()) {
					throw new EmailAlreadyUsedException();
				} else {

					user.setEmail(email.toLowerCase());
				}
			}
			if (password != null && !password.isBlank()) {
				String encryptedPassword = passwordEncoder.encode(password);
				user.setPassword(encryptedPassword);
			}
			if (adresse != null) {
				user.setAdresse(adresse);
			}
			if (telephone != null) {
				user.setTelephone(telephone);
			}
			if (dateNaissance != null) {
				user.setDateNaissance(dateNaissance);
			}

			log.debug("Existing Authorities for User: {}", user.getRoles());

			userRepository.save(user);

			log.debug("Changed Information for User: {}", user);
		});
	}

	public List<ServiceAdditionnel> getAllService(){
		return serviceAddRepo.findAll();
	}
	

	private User getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
