package com.example.study.damain.service;

import com.example.study.damain.entity.Member;
import com.example.study.damain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetatilsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        List<SimpleGrantedAuthority> grantedAuthorities = findMember.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                .collect(Collectors.toList());

        return new User(findMember.getEmail(), findMember.getPassword(), grantedAuthorities);
    }
}
