package com.balu.LibraryManagementSystem.controller;

import com.balu.LibraryManagementSystem.dto.BookDTO;
import com.balu.LibraryManagementSystem.dto.BorrowRequestDTO;
import com.balu.LibraryManagementSystem.dto.BorrowResponseDTO;
import com.balu.LibraryManagementSystem.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    // POST /api/borrow
    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<BorrowResponseDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(dto));
    }

    // PUT /api/borrow/1/return
    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.returnBook(id));
    }

    // GET /api/borrow/member/1
    @GetMapping("/member/{memberId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BorrowResponseDTO>> getBorrowsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowService.getBorrowsByMember(memberId));
    }

    // GET /api/borrow/book/1
    @GetMapping("/book/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BorrowResponseDTO>> getBorrowsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.getBorrowsByBook(bookId));
    }
}
