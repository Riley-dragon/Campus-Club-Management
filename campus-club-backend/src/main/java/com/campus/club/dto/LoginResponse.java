package com.campus.club.dto;

import com.campus.club.entity.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
