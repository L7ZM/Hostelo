package com.udev.hotel.service.mapper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.udev.hotel.domain.entity.Role;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.service.dto.UserDTO;

@Service
public class UserMapper {

	public UserDTO userToUserDTO(User user) {
		return new UserDTO(user);
	}

	public List<UserDTO> usersToUserDTOs(List<User> users) {
		return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
	}

	public User userDTOToUser(UserDTO userDTO) {
		 if (userDTO == null) {
	            return null;
	        } else {
	            User user = new User();
	            user.setId(userDTO.getId());
	            user.setNom(userDTO.getNom());
	            user.setPrenom(userDTO.getPrenom());
	            user.setEmail(userDTO.getEmail());
	            user.setPassword(userDTO.getPassword());
	            user.setAdresse(userDTO.getAdresse());
	            user.setTelephone(userDTO.getTelephone());
	            user.setPointsFidelite(userDTO.getPointsFidelite());
	            user.setDateNaissance(userDTO.getDateNaissance());
	            Set<Role> roles = this.authoritiesFromStrings(userDTO.getAuthorities());
	            if (roles != null) {
	                user.setRoles(roles);
	            }
	            return user;
	        }
		
	}

	public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
		return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
	}

	public User userFromId(Long id) {
		if (id == null) {
			return null;
		}
		User user = new User();
		user.setId(id);
		return user;
	}

	public Set<Role> authoritiesFromStrings(Set<String> strings) {
		return strings.stream().map(string -> {
			Role auth = new Role();
			auth.setName(string);
			return auth;
		}).collect(Collectors.toSet());
	}

}
