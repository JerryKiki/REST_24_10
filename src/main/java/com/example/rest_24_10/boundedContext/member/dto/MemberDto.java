package com.example.rest_24_10.boundedContext.member.dto;

import com.example.rest_24_10.boundedContext.member.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

//Member의 Entity가 그대로 외부로 노출되면 곤란해질 수 있기 때문에 사용...
//밖에서 보여지는 '디자인'을 바꾼다고 보면 된다 (스킨 씌워서 보여주는 느낌?)
//외부 노출용 데이터. 외부 API에 보여줘야 하는 데이터로 다시 써줘야한다.
//이 양식은 변하지 않을 것이다.
//초반에는 필요없을 수 있지만, 어느정도 방향성이 정해지면 외부 API에서 바라는 요청과 내 내부 개발용이 달라질 수 있음
//정체는 똑같은데 용어만 밖에 맞춰주기 위해 사용

@Data
public class MemberDto {
    private Long id;
    private LocalDateTime regDate;
    private String userName;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.regDate = member.getCreateDate();
        this.userName = member.getUsername();
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member);
    }
}
