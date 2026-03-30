package com.balu.LibraryManagementSystem.service;

import com.balu.LibraryManagementSystem.dto.LoginRequestDto;
import com.balu.LibraryManagementSystem.dto.MemberRequestDto;
import com.balu.LibraryManagementSystem.dto.MemberResponseDto;
import com.balu.LibraryManagementSystem.entity.Member;
import com.balu.LibraryManagementSystem.exception.DuplicateEmailException;
import com.balu.LibraryManagementSystem.exception.InvalidCredentialsException;
import com.balu.LibraryManagementSystem.exception.ResourceNotFoundException;
import com.balu.LibraryManagementSystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // REGISTER
    public MemberResponseDto register(MemberRequestDto dto) {
        // Check if email already exists
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already registered : " + dto.getEmail());
        }
        Member member = new Member();
        member.setFullName(dto.getFullName());
        member.setEmail(dto.getEmail());
        member.setPassword(encoder.encode(dto.getPassword()));
        member.setPhone(dto.getPhone());

        Member saved = memberRepository.save(member);
        return mapToDto(saved);
    }

    // LOGIN
    public MemberResponseDto login(LoginRequestDto dto) {
        // Check if email present or not
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + dto.getEmail()));
        // Check if password matches with member and dto
        if (!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new InvalidCredentialsException("Invalid Password");
        }
        return mapToDto(member);
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Mapper
    private MemberResponseDto mapToDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhone(),
                member.getMembershipDate(),
                member.getCreatedDate()
        );
    }
}
