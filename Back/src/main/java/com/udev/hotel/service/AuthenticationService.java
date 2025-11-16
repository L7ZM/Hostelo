package com.udev.hotel.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.udev.hotel.config.security.AuthoritiesConstants;
import com.udev.hotel.domain.entity.Role;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.domain.repository.RoleRepository;
import com.udev.hotel.domain.repository.UserRepository;
import com.udev.hotel.service.dto.LoginUserDto;
import com.udev.hotel.service.dto.RegisterUserDto;
import com.udev.hotel.service.dto.UserDTO;

@Service
public class AuthenticationService {
	
	private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    
	private final RoleRepository roleRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(UserDTO userDTO) {
    	User newUser = new User();
		Role authority = roleRepository.findByName(AuthoritiesConstants.USER)
				.orElseThrow(() -> new IllegalArgumentException("Role not found: " + AuthoritiesConstants.USER));

		Set<Role> authorities = new HashSet<>();
		newUser.setEmail(userDTO.getEmail());
	    if (userDTO.getPassword() != null) {
	        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
	        log.debug(encryptedPassword); 
	        newUser.setPassword(encryptedPassword);
	    } else {
	        throw new IllegalArgumentException("Password cannot be null");
	    }		
	    newUser.setPrenom(userDTO.getPrenom());
		newUser.setNom(userDTO.getNom());
		newUser.setAdresse(userDTO.getAdresse());
		newUser.setDateNaissance(userDTO.getDateNaissance());
		newUser.setTelephone(userDTO.getTelephone());
		authorities.add(authority);
		newUser.setRoles(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);

        return userRepository.save(newUser);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}