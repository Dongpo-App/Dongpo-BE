package com.dongyang.dongpo.domain.member.entity;

import lombok.Getter;

@Getter
public enum Title {
    BASIC_TITLE("막 개장한 포장마차", "첫 가입 시"),
    FAILED_TO_VISIT("실패는 성공의 어머니", "3회 이상 방문 실패"),
    REGULAR_CUSTOMER("난 한 놈만 패", "3회 이상 같은 점포 방문"),
    FIRST_VISIT_CERT("첫 발을 디딘 자", "첫 방문 인증 시도"),
    FIRST_STORE_REGISTER("개척자", "점포를 처음 등록 한 사람"),
    REVIEW_PRO("프로 리뷰어", "3회 이상 리뷰 등록"),
    REGISTER_PRO("점포 스카우터", "3곳 이상 점포 등록"),
    ;

    private final String description;
    private final String achieveCondition;

    Title(String description, String achieveCondition) {
        this.description = description;
        this.achieveCondition = achieveCondition;
    }
}
