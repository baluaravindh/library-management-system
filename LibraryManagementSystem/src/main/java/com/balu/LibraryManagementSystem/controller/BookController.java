package com.balu.LibraryManagementSystem.controller;

import com.balu.LibraryManagementSystem.dto.BookDTO;
import com.balu.LibraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // POST /api/books
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO dto) {
        BookDTO created = bookService.addBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/books
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // GET by id /api/books/1
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // GET by genre /api/books/genre/{genre}
    @GetMapping("/genre")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> getAllBooksByGenre(@RequestParam String genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    // Keyword search in genre (new)
    @GetMapping("/genre/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> searchByGenre(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.getBooksByGenreKeyword(keyword));
    }

    // GET by genre /api/books/author/{author}
    @GetMapping("/author/{author}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> getAllBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    // UPDATE by id /api/books/1
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable Long id, @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.updateBookById(id, dto));
    }

    // DELETE by id /api/books/1
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book Deleted Successfully with id : " + id);
    }
}
