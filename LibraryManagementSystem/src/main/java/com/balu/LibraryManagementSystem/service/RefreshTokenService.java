package com.balu.LibraryManagementSystem.service;

import com.balu.LibraryManagementSystem.dto.RefreshTokenRequestDto;
import com.balu.LibraryManagementSystem.entity.Member;
import com.balu.LibraryManagementSystem.entity.RefreshToken;
import com.balu.LibraryManagementSystem.exception.ResourceNotFoundException;
import com.balu.LibraryManagementSystem.repository.MemberRepository;
import com.balu.LibraryManagementSystem.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // CREATE a new refresh token for a user
    @Transactional
    public RefreshToken createRefreshToken(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + memberId));

        // Delete any existing refresh token for this user
        refreshTokenRepository.deleteByMemberId(memberId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setMember(member);
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    // VALIDATE refresh token
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired. Please login again.");
        }
        return refreshToken;
    }

    // REVOKE on logout
    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found."));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
