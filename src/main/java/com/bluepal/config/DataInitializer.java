package com.bluepal.config;

import com.bluepal.dto.UserDTO;
import com.bluepal.entity.User;
import com.bluepal.service.UserServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserServiceImpl userService;

    // Constructor injection with @Lazy to break circular dependency
    public DataInitializer(@Lazy UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userService.findEntityByEmail("admin@example.com").isPresent()) {
            UserDTO adminDTO = new UserDTO();
            adminDTO.setName("Admin User");
            adminDTO.setEmail("admin@example.com");
            adminDTO.setPassword("admin123");  // Password encoding should be handled inside service
            adminDTO.setPhone("1234567890");
            adminDTO.setRole(User.Role.ADMIN); // Ensure UserDTO has this field

            userService.createUser(adminDTO);
            System.out.println("Admin user created: admin@example.com / admin123");
        }

        // Create regular user if not exists
        if (!userService.findEntityByEmail("user@example.com").isPresent()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setName("Regular User");
            userDTO.setEmail("user@example.com");
            userDTO.setPassword("user123");
            userDTO.setPhone("0987654321");
            userDTO.setRole(User.Role.USER);

            userService.createUser(userDTO);
            System.out.println("Regular user created: user@example.com / user123");
        }
    }
}
