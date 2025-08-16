package com.bluepal.service;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import com.bluepal.dto.AuthRequest;
import com.bluepal.dto.AuthResponse;
import com.bluepal.dto.UserDTO;
import com.bluepal.entity.User;

public interface UserService {
    AuthResponse authenticateUser(AuthRequest loginRequest);
    ResponseEntity<?> registerUser(UserDTO signUpRequest);
    UserDTO createUser(UserDTO userDTO);
    Optional<User> findEntityByEmail(String email);
    UserDTO findById(Long id);
}
