package com.balu.LibraryManagementSystem.service;

import com.balu.LibraryManagementSystem.dto.BookDTO;
import com.balu.LibraryManagementSystem.entity.Book;
import com.balu.LibraryManagementSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    //Create
    public BookDTO addBook(BookDTO dto) {
        Book book = mapToEntity(dto);
        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    // Get All Books
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get By Id
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found with id : " + id));
        return mapToDto(book);
    }

    // Get books by genre
    public List<BookDTO> getBooksByGenre(String genre) {
        return bookRepository.findBooksByGenre(genre)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get books by author
    public List<BookDTO> getBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthor(author)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update books by Id
    public BookDTO updateBookById(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found with id : " + id));
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setGenre(dto.getGenre());
        book.setAvailableCopies(dto.getAvailableCopies());

        Book updated = bookRepository.save(book);
        return mapToDto(updated);
    }

    // Delete book by Id
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found with id : " + id));
        bookRepository.delete(book);
    }

    // ---Mapper---
    private BookDTO mapToDto(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setGenre(book.getGenre());
        dto.setAvailableCopies(book.getAvailableCopies());
        return dto;
    }

    private Book mapToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setGenre(dto.getGenre());
        book.setAvailableCopies(dto.getAvailableCopies());
        return book;
    }
}
