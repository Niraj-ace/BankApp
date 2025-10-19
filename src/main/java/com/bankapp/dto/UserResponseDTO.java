package com.bankapp.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

	private Long id;
    private String username;
    private String password;
    private String email;  
    private String role;
    private List<String> authorities;
    private boolean accountNonExpire;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    public boolean enabled;
}
