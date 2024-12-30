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
    TOKEN_ISSUER_MISMATCH(401, "토큰 발급자가 일치하지 않습니다."),
    TOKEN_AUDIENCE_MISMATCH(401, "토큰 수신 대상자가 일치하지 않습니다."),

    DISTANCE_OUT_OF_RANGE(400, "위치 정보가 오차를 벗어났습니다."),
    ARGUMENT_NOT_SATISFIED(400, "요청이 잘못되었습니다."),
    BOOKMARK_ALREADY_EXISTS(400, "해당 점포의 북마크가 이미 존재합니다."),
	REPORT_REASON_TEXT_REQUIRED(400, "기타 사유는 사유를 작성해야 합니다."),
    ADDITIONAL_INFO_REQUIRED_FOR_SIGNUP(400, "회원 가입에 필요한 추가 정보 요청."),
    APPLE_AUTHORIZATION_CODE_EXPIRED(400, "Apple 인증 코드가 만료되었습니다."),
    APPLE_PUBLIC_KEY_GENERATION_FAILED(400, "Apple 공개키 생성에 실패하였습니다."),
    HEADER_PARSING_FAILED(400, "헤더 파싱에 실패하였습니다."),
    CLAIMS_EXTRACTION_FAILED(400, "토큰 정보 추출에 실패하였습니다."),
    SERVICE_TERMS_NOT_AGREED(400, "서비스 이용 약관에 동의 하지 않았습니다."),

    UNAUTHORIZED(403, "권한이 없습니다."),
    MEMBER_ALREADY_LEFT(403, "탈퇴한 회원입니다."),
    UNSUPPORTED_AUTHENTICATION_TYPE(403, "지원하지 않는 인증 방식입니다."),

    MEMBER_EMAIL_DUPLICATED(409, "이미 사용중인 이메일입니다."),
    MEMBER_ALREADY_EXISTS(409, "이미 가입된 회원입니다."),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
