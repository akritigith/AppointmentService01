package com.bluepal.dto;

import com.bluepal.entity.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private User.Role role;

    public AuthResponse(String token, Long id, String name, String email, User.Role role) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
