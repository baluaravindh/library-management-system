package com.balu.LibraryManagementSystem.repository;

import com.balu.LibraryManagementSystem.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByToken(String token);

    void deleteByMemberId(Long memberId);
}
