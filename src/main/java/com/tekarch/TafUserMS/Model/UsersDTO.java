package com.tekarch.TafUserMS.Model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersDTO {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
