package com.example.study.api.auth;

import com.example.study.api.exception.ValidationException;
import com.example.study.security.dto.LoginDto;
import com.example.study.security.dto.TokenDto;
import com.example.study.security.jwt.JwtAuthenticationFilter;
import com.example.study.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/auth")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationException("로그인 에러", bindingResult);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());


        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createAccessToke(authenticationToken);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + token);

        return new ResponseEntity<>(new TokenDto(token), headers, HttpStatus.OK);
    }
}
