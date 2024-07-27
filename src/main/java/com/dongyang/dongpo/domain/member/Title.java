package com.dongyang.dongpo.domain.member;

import lombok.Getter;

@Getter
public enum Title {
    BASIC_TITLE("막 개장한 포장마차"), // 첫 가입시
    FAILED_TO_VISIT("실패는 성공의 어머니"), // n회 이상 방문 실패시
    REGULAR_CUSTOMER("난 한 놈만 패"), // n회 이상 같은 점포 방문시
    ;

    private final String description;

    Title(String description) {
        this.description = description;
    }
}
