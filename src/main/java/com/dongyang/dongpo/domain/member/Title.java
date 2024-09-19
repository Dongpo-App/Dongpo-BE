package com.dongyang.dongpo.domain.member;

import lombok.Getter;

@Getter
public enum Title {
    BASIC_TITLE("막 개장한 포장마차"), // 첫 가입시
    FAILED_TO_VISIT("실패는 성공의 어머니"), // 3회 이상 방문 실패시
    REGULAR_CUSTOMER("난 한 놈만 패"), // 3회 이상 같은 점포 방문시
    FIRST_VISIT_CERT("첫 발을 디딘 자"), // 처음 방문인증 한 사람
    FIRST_STORE_REGISTER("개척자"), // 처음으로 점포를 등록한 사람
    REVIEW_PRO("프로 리뷰어"), // 3회 이상 리뷰 등록한 사람
    REGISTER_PRO("점포 스카우터"), // 점포를 3곳 이상 등록한 사람
    ;

    private final String description;

    Title(String description) {
        this.description = description;
    }
}
