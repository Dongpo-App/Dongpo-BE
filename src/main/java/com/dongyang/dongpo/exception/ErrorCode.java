package com.dongyang.dongpo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MEMBER_NOT_FOUND(404, "회원을 찾지 못하였습니다."),
    BOOKMARK_NOT_FOUND(404, "북마크를 찾지 못하였습니다."),
    STORE_NOT_FOUND(404, "점포를 찾지 못하였습니다."),
    REVIEW_NOT_FOUND(404, "리뷰를 찾기 못하였습니다."),

    SOCIAL_TOKEN_NOT_VALID(401, "소셜토큰이 유효하지 않습니다."),
    CLAIMS_NOT_VALID(401, "토큰 정보가 유효하지 않습니다."),
    CLAIMS_NOT_FOUND(401, "토큰 정보를 찾을 수 없습니다."),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(401, "지원하지 않는 토큰입니다."),
    MALFORMED_TOKEN(401, "토큰이 잘못되었습니다."),

    STORE_REGISTRATION_NOT_VALID(400, "위치 정보가 오차를 벗어났습니다."),
    ARGUMENT_NOT_SATISFIED(400, "요청이 잘못되었습니다."),
    BOOKMARK_ALREADY_EXISTS(400, "해당 점포의 북마크가 이미 존재합니다."),
	REPORT_REASON_TEXT_REQUIRED(400, "기타 사유는 사유를 작성해야 합니다."),;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
