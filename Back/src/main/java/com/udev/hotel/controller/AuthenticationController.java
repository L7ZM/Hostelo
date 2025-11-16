package com.udev.hotel.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udev.hotel.config.LoginResponse;
import com.udev.hotel.domain.entity.Role;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.service.AuthenticationService;
import com.udev.hotel.service.JwtService;
import com.udev.hotel.service.dto.LoginUserDto;
import com.udev.hotel.service.dto.UserDTO;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {
    	
        log.debug("Received UserDTO: {}", userDTO); 
        User registeredUser = authenticationService.signup(userDTO);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        List<String> roles = authenticatedUser.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime())
                .setId(authenticatedUser.getId())
                .setUsername(authenticatedUser.getUsername())
                .setNom(authenticatedUser.getNom())
                .setPrenom(authenticatedUser.getPrenom())
                .setEmail(authenticatedUser.getEmail())
                .setAdresse(authenticatedUser.getAdresse())
                .setDateNaissance(authenticatedUser.getDateNaissance())
                .setTelephone(authenticatedUser.getTelephone())
                .setRoles(roles)
        		.setPointsFidelite(authenticatedUser.getPointsFidelite());
        log.info(authenticatedUser.getUsername());
        return ResponseEntity.ok(loginResponse);
    }
    
}