package com.example.rest_24_10.base.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // 아래의 모든 설정은 /api/** 경로에만 적용
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/*/member/login").permitAll() // 로그인은 누구나 가능
                                .anyRequest().authenticated() // 나머지는 인증된 사용자만 가능
                )
                .cors().disable() // 타 도메인에서 API 호출 가능
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBasic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                ); // 세션 끄기

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/*
스프링 시큐리티 => 얘로 궁극적으로 하고 싶은 것 : 로그인/로그아웃 할때... 이 회원이 누군지, 뭘 할 수 있는지

1. 일반적인 방식
(0) 서버가 클라이언트에서 ID/PW를 받으면...
(1) DB 통해 유효성 검사
(2) 통과하면... User/Member가 맞다고 객체화해서 따로 Http 세션에 저장 (유저를 기억하고 있음)
(3) 이 때 유효기간 : 로그아웃 / 세션 만료 전(시간 설정 전제 하에)
(4) 로그인 후, 요청 보낼 때마다 세션키(==쿠키)를 계속 같이 실어서 날림 (세션키는 매 요청마다 서버 측으로 전달된다)
==> 유저 객체를 로그인할 당시에 만들어놓고, 얘를 저장해놓고 계속 사용한다

2. REST API : 얘는 세션이 없는 환경이다! '일반적인 방식'처럼 유저를 구분하고 하려면 어떻게 해야할까?
(0) 서버가 클라이언트에서 ID/PW를 받으면...
(1) 어차피 저장을 못하기 때문에 User/Member 객체를 만들지 않는다. 대신, Access Token을 만든다. (유저를 기억하고 있지 않음)
(2) 서버에서 Access Token을 클라이언트에 줌
(3) 클라이언트가 요청을 보낼 때마다 Access Token을 태워 보낸다 (액세스 토큰 정보는 매 요청마다 서버 측으로 전달된다)
(4) 서버는 Access Token이 유효한지 검사한 뒤 요청을 수용한다
(5) JWT로 만들어진 유의미한 유저 정보가 Access Token에 담겨있다 ==> 이 정보를 기반으로 한 어떤 객체를 매번 만든다
(6) 그 객체에 대한 정보를 Spring Security에 등록시킨다
(7) 요청에 대해 응답할 때 등록되어있던 정보가 다시 죽는다 (==유저를 등록해놔도 유지되지는 않는다)
==> 유저 객체를 저장하지는 않지만, 신경쓰지 않아도 시큐리티가 알아서 해줌.
==> UserDetailsService는 기본적으로 존재하되, 커스텀하고 싶다면 이 프로젝트의 CustomUserDetailService처럼 상속받고 오버라이드해서 쓰면 됨
==> 결론 : Access Token 넘겨줄 때 유저 만들고 등록을 시켜준다.
*/