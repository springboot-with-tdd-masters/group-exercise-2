package com.example.groupexercise2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.groupexercise2.model.User;
import com.example.groupexercise2.repository.UserRepository;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
    @Override 
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	User user = new User();
    	user.setUsername("admin");
    	user.setPassword(encoder.encode("Welcome123"));
    	
    	userRepository.save(user);
    }
}