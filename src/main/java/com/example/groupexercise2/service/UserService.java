package com.example.groupexercise2.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.groupexercise2.model.User;
import com.example.groupexercise2.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);		
		return Optional.ofNullable(user)
			.map(u -> new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), getAuthorityList()))
			.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
	}
	
	private List<GrantedAuthority> getAuthorityList() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
