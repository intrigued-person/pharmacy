package com.sahana;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sahana.config.JwtTokenProvider;
import com.sahana.exception.UserException;
import com.sahana.modal.User;
import com.sahana.repository.UserRepository;
import com.sahana.service.UserServiceImplementation;

public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserById_Success() throws UserException {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(1L);

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void testFindUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> {
            userService.findUserById(1L);
        });
    }

    @Test
    void testFindUserProfileByJwt_Success() throws UserException {
        String jwt = "valid.jwt.token";
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        when(jwtTokenProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findUserProfileByJwt(jwt);

        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void testFindUserProfileByJwt_NotFound() {
        String jwt = "valid.jwt.token";
        String email = "test@example.com";

        when(jwtTokenProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserException.class, () -> {
            userService.findUserProfileByJwt(jwt);
        });
    }

    @Test
    void testFindAllUsers() {
        // You can add more specific tests if needed
    }
}
