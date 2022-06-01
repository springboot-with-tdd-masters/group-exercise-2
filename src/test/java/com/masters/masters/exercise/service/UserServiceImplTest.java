package com.masters.masters.exercise.service;

import com.masters.masters.exercise.model.User;
import com.masters.masters.exercise.repository.UserRepository;
import com.masters.masters.exercise.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl service;
    
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void loadUserByUsername(){
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        org.springframework.security.core.userdetails.UserDetails userDetails = service.loadUserByUsername("test");
        Assertions.assertEquals("test",userDetails.getUsername());
        Assertions.assertEquals("password",userDetails.getPassword());
    }

    @Test
    public void findAll(){
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("password");
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> userList = service.findAll();
        Assertions.assertEquals(1,userList.size());
        Assertions.assertEquals("password",userList.get(0).getPassword());
        Assertions.assertEquals("test",userList.get(0).getUsername());
    }
}
