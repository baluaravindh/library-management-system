package com.balu.LibraryManagementSystem.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDateTime membershipDate;
    private LocalDateTime createdDate;
}
