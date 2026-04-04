package com.balu.LibraryManagementSystem.controller;

import com.balu.LibraryManagementSystem.dto.*;
import com.balu.LibraryManagementSystem.entity.Member;
import com.balu.LibraryManagementSystem.entity.RefreshToken;
import com.balu.LibraryManagementSystem.security.JwtUtil;
import com.balu.LibraryManagementSystem.service.MemberService;
import com.balu.LibraryManagementSystem.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // POST /api/member/register
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(@Valid @RequestBody MemberRequestDto dto) {
        MemberResponseDto registerResponseDto = memberService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDto);
    }

    // POST /api/member/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginMember(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto loginResponseDto = memberService.login(dto);
        return ResponseEntity.ok(loginResponseDto);
    }

    // GET /api/member
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // POST /api/users/refresh
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto dto) {

        // Validate refresh token
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(dto.getRefreshToken());

        Member member = refreshToken.getMember();

        // Generate new access token
        String newAccessToken = jwtUtil.generateToken(
                member.getEmail(),
                member.getRole().name());

        // Generate new refresh token (rotate for security)
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(member.getId());

        LoginResponseDto responseDto = new LoginResponseDto(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getRole().name(),
                newAccessToken,
                "Bearer",
                newRefreshToken.getToken()
        );
        return ResponseEntity.ok(responseDto);
    }

    // POST /api/users/logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequestDto dto) {
        refreshTokenService.revokeRefreshToken(dto.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully.");
    }
}
