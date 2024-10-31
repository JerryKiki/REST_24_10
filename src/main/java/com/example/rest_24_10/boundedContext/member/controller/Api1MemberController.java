package com.example.rest_24_10.boundedContext.member.controller;

import com.example.rest_24_10.base.rsData.RsData;
import com.example.rest_24_10.boundedContext.member.entity.Member;
import com.example.rest_24_10.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
// /api/v1 이런거 붙여 주는 (=='버저닝' 하는) 이유 : 각 개발 환경에서의 호환성을 관리하기 수월하도록 하기 위해
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class Api1MemberController {

    private final MemberService memberService;

    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @AllArgsConstructor
    @Getter
    public static class LoginResponse {
        private final String accessToken;
    }

    @PostMapping("/login")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) { //, HttpServletResponse resp
        String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getPassword());

//        resp.addHeader("Authentication", accessToken); //헤더에만 추가하는 애라 딱히 필요 없음

        return RsData.of("S-1", "액세스 토큰 생성됨", new LoginResponse(accessToken));
    }

    @AllArgsConstructor
    @Getter
    public static class MeResponse {
        private final Member member;
    }

    //consumes = ALL_VALUE -> json 형태로 입력 받는 게 필수가 아니란 뜻
    @GetMapping(value = "/me", consumes = ALL_VALUE)
    public RsData<MeResponse> me() { //, HttpServletResponse resp
       Member member = memberService.findByUsername("user1").get();

//        resp.addHeader("Authentication", accessToken); //헤더에만 추가하는 애라 딱히 필요 없음

        return RsData.of("S-1", "액세스 토큰 생성됨", new MeResponse(member));
    }

}
