package com.udev.hotel.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udev.hotel.config.constants.Constants;
import com.udev.hotel.config.security.AuthoritiesConstants;
import com.udev.hotel.controller.exceptionHandler.BadRequestAlertException;
import com.udev.hotel.controller.exceptionHandler.EmailAlreadyUsedException;
import com.udev.hotel.controller.exceptionHandler.util.HeaderUtil;
import com.udev.hotel.domain.entity.ServiceAdditionnel;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.domain.repository.ServiceAdditionnelRepository;
import com.udev.hotel.domain.repository.UserRepository;
import com.udev.hotel.service.AdminService;
import com.udev.hotel.service.dto.UserDTO;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ServiceAdditionnelRepository serviceAddRepo;
	private final AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {

		this.adminService = adminService;
	}

	@PostMapping("/createUser")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
		log.debug("REST request to save User : {}", userDTO);

		if (userDTO.getId() != null) {
			throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
		} else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
			throw new EmailAlreadyUsedException();
		} else {
			User newUser = adminService.createUser(userDTO);
			log.info(newUser.toString());
			return ResponseEntity.created(new URI("/api/users/" + newUser.getEmail())).body(newUser);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO, @RequestParam String password) {

		User newUser = adminService.registerUser(userDTO, password);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	/**
	 * DELETE /users/:login : delete the "login" User.
	 *
	 * @param login the login of the user to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/users/{email:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable String email) {
		log.debug("REST request to delete User: {}", email);
		adminService.deleteUser(email);
		return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + email, email))
				.build();
	}

	/**
	 * GET /users : get all users.
	 * 
	 * @return the ResponseEntity with status 200 (OK) and with body all users
	 */
	@GetMapping("/users")
	@Timed
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = adminService.allUsers();
		return ResponseEntity.ok(users);
	}

	/**
	 * @return a string list of the all of the roles
	 */
	@GetMapping("/users/authorities")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public List<String> getAuthorities() {
		return adminService.getAuthorities();
	}

	@PutMapping("/updateUser")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
		log.debug("REST request to update User: {}", userDTO);

		Optional<User> existingUserByEmail = userRepository.findByEmail(userDTO.getEmail());
		if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(userDTO.getId())) {
			throw new EmailAlreadyUsedException();
		}

		Optional<UserDTO> updatedUser = adminService.updateUser(userDTO);

		if (updatedUser.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().headers(
				HeaderUtil.createAlert("A user is updated with identifier " + userDTO.getEmail(), userDTO.getEmail()))
				.body(updatedUser.get());
	}

	/**
	 * GET /user/:idUser:
	 * 
	 * @param idUser
	 * @return the ResponseEntity with status 200 (OK) and with body the "login"
	 *         user, or with status 404 (Not Found)
	 */
	@GetMapping("/user/{idUser}")
	@Timed
	public ResponseEntity<UserDTO> findUserById(@PathVariable("idUser") Long idUser) {
		log.debug("REST request to get User : {}", idUser);

		Optional<UserDTO> user = Optional.ofNullable(userRepository.findUserById(idUser));

		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/serviceAdditionnel")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<ServiceAdditionnel> createOrUpdateServiceAdditionnel(@RequestParam String nomService,
			@RequestParam String description, @RequestParam Double prix) {
		ServiceAdditionnel newServiceAdd = adminService.addOrUpdateServiceAdditionnel(nomService, description, prix);
		return ResponseEntity.ok().headers(
				HeaderUtil.createAlert("A service additionnel is created with identifier " + nomService, nomService))
				.body(newServiceAdd);
	}
	
	
	@GetMapping("/serviceAdditionnel")
	@Timed
	public ResponseEntity<List<ServiceAdditionnel>> getAllServiceAdditionnel() {
		List<ServiceAdditionnel> serviceAdditionnel = adminService.getAllService();
		return ResponseEntity.ok(serviceAdditionnel);
	}

	
	@DeleteMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        adminService.deleteServiceAdditionnel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

	@PutMapping("/update")
	@Timed
	public ResponseEntity<Void> update(@RequestBody UserDTO updateUserDto) {
		log.debug("REST request to update user information: {}", updateUserDto);

		adminService.updateUser(updateUserDto.getPrenom(), updateUserDto.getNom(), updateUserDto.getEmail(),
				updateUserDto.getPassword(), updateUserDto.getAdresse(), updateUserDto.getTelephone(),
				updateUserDto.getDateNaissance());

		return ResponseEntity.ok().headers(HeaderUtil
				.createAlert("A user is updated with identifier " + updateUserDto.getEmail(), updateUserDto.getEmail()))
				.build();
	}

}
