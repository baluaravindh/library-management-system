package com.balu.LibraryManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowResponseDTO {

    private Long id;
    private String memberName;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;
}
