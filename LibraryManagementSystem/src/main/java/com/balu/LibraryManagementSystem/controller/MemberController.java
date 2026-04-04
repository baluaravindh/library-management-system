package com.balu.LibraryManagementSystem.controller;

import com.balu.LibraryManagementSystem.dto.LoginRequestDto;
import com.balu.LibraryManagementSystem.dto.LoginResponseDto;
import com.balu.LibraryManagementSystem.dto.MemberRequestDto;
import com.balu.LibraryManagementSystem.dto.MemberResponseDto;
import com.balu.LibraryManagementSystem.security.JwtUtil;
import com.balu.LibraryManagementSystem.service.MemberService;
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
    private final JwtUtil  jwtUtil;

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
}
