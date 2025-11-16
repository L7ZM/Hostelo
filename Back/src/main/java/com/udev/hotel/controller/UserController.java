package com.udev.hotel.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udev.hotel.controller.exceptionHandler.util.HeaderUtil;
import com.udev.hotel.domain.entity.ServiceAdditionnel;
import com.udev.hotel.domain.repository.UserRepository;
import com.udev.hotel.service.UserService;
import com.udev.hotel.service.dto.UserDTO;
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {

		this.userService = userService;
	}

	@PutMapping("/update")
	@Timed
	public ResponseEntity<Void> updateUser(@RequestBody UserDTO updateUserDto) {
		log.debug("REST request to update user information: {}", updateUserDto);

		userService.updateUser(updateUserDto.getPrenom(), updateUserDto.getNom(), updateUserDto.getEmail(),
				updateUserDto.getPassword(), updateUserDto.getAdresse(), updateUserDto.getTelephone(),
				updateUserDto.getDateNaissance());

		return ResponseEntity.ok().headers(HeaderUtil
				.createAlert("A user is updated with identifier " + updateUserDto.getEmail(), updateUserDto.getEmail()))
				.build();
	}
	
	@GetMapping("/serviceAdditionnel")
	@Timed
	public ResponseEntity<List<ServiceAdditionnel>> getAllServiceAdditionnel() {
		List<ServiceAdditionnel> serviceAdditionnel = userService.getAllService();
		return ResponseEntity.ok(serviceAdditionnel);
	}
}
