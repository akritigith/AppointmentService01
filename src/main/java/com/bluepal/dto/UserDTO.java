package com.bluepal.dto;

import com.bluepal.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String phone;       // Add this
    private User.Role role;     
    private String confirmPassword; // Only for validation; not saved

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
