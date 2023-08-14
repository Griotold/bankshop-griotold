package com.griotold.bankshop.ztudy;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;

    /*
    * @RequestParam 어노테이션을 사용하지 않아도
    * Spring MVC는 기본적으로 URL 쿼리 파라미터를
    * DTO 객체의 필드와 자동으로 매핑해줍니다
    * */
    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
