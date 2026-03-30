package com.balu.LibraryManagementSystem.repository;

import com.balu.LibraryManagementSystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBooksByGenre(String genre);

    List<Book> findBooksByAuthor(String author);

    List<Book> findByGenreIsContainingIgnoreCase(String keyword);

    List<Book> findBooksByAuthorContainingIgnoreCase(String keyword);
}
