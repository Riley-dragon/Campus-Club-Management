package com.campus.club.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String realName;
    private String studentId;
    private String college;
    private String major;
    private String email;
    private String phone;
}
