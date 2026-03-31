package com.balu.LibraryManagementSystem.service;

import com.balu.LibraryManagementSystem.dto.BorrowRequestDTO;
import com.balu.LibraryManagementSystem.dto.BorrowResponseDTO;
import com.balu.LibraryManagementSystem.entity.Book;
import com.balu.LibraryManagementSystem.entity.BorrowRecord;
import com.balu.LibraryManagementSystem.entity.Member;
import com.balu.LibraryManagementSystem.exception.ResourceNotFoundException;
import com.balu.LibraryManagementSystem.repository.BookRepository;
import com.balu.LibraryManagementSystem.repository.BorrowRecordRepository;
import com.balu.LibraryManagementSystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    // BORROW A BOOK
    @Transactional
    public BorrowResponseDTO borrowBook(BorrowRequestDTO dto) {
        // Step 1: Find member
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + dto.getMemberId()));

        // Step 2: Find book
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId()));

        // Step 3: Check availability
        if (book.getAvailableCopies() == 0) {
            throw new RuntimeException("No copies available for: " + book.getTitle());
        }

        // Step 4: Create borrow record
        BorrowRecord record = new BorrowRecord();
        record.setMember(member);
        record.setBook(book);

        // Step 5: Deduct available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Step 6: Save and return
        BorrowRecord saved = borrowRecordRepository.save(record);
        return mapToDto(saved);
    }

    // RETURN A BOOK
    @Transactional
    public BorrowResponseDTO returnBook(Long borrowRecordId) {
        // Step 1: Find the borrow record
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + borrowRecordId));

        // Step 2: Check if already returned
        if (record.getStatus() == BorrowRecord.BorrowStatus.RETURNED) {
            throw new RuntimeException("This book has already been returned.");
        }

        // Step 3: Update record
        record.setStatus(BorrowRecord.BorrowStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        // Step 4: Add copy back to book
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Step 5: Save and return
        BorrowRecord updated = borrowRecordRepository.save(record);
        return mapToDto(updated);
    }

    // GET ALL BORROWS BY MEMBER
    public List<BorrowResponseDTO> getBorrowsByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found with id: " + memberId);
        }
        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // GET ALL BORROWS BY BOOK
    public List<BorrowResponseDTO> getBorrowsByBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }
        return borrowRecordRepository.findByBookId(bookId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // MAPPER
    private BorrowResponseDTO mapToDto(BorrowRecord record) {
        return new BorrowResponseDTO(
                record.getId(),
                record.getMember().getFullName(),
                record.getBook().getTitle(),
                record.getBorrowDate(),
                record.getReturnDate(),
                record.getStatus().name()
        );
    }
}
