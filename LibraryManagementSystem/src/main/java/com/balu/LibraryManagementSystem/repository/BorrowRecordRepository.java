package com.balu.LibraryManagementSystem.repository;

import com.balu.LibraryManagementSystem.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {


    List<BorrowRecord> findByMemberId(Long memberId);

    List<BorrowRecord> findByBookId(Long bookId);
}
