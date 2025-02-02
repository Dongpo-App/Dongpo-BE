package com.dongyang.dongpo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 인증 관련 예외
    SOCIAL_TOKEN_NOT_VALID(401, "A001", "소셜토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(401, "A002", "토큰이 만료되었습니다."),
    HEADER_PARSING_FAILED(401, "A003", "헤더 파싱에 실패하였습니다."),
    CLAIMS_EXTRACTION_FAILED(401, "A004", "토큰 정보 추출에 실패하였습니다."),
    CLAIMS_NOT_VALID(401, "A005", "토큰 정보가 유효하지 않습니다."),
    CLAIMS_NOT_FOUND(401, "A006", "토큰 정보를 찾을 수 없습니다."),
    UNSUPPORTED_TOKEN(401, "A007", "지원하지 않는 토큰입니다."),
    MALFORMED_TOKEN(401, "A008", "토큰이 잘못되었습니다."),
    TOKEN_ISSUER_MISMATCH(401, "A009", "토큰 발급자가 일치하지 않습니다."),
    TOKEN_AUDIENCE_MISMATCH(401, "A010", "토큰 수신 대상자가 일치하지 않습니다."),
    UNSUPPORTED_AUTHENTICATION_TYPE(401, "A011", "지원하지 않는 인증 방식입니다."),
    ADDITIONAL_INFO_REQUIRED_FOR_SIGNUP(401, "A012", "회원 가입에 필요한 추가 정보 요청."),
    APPLE_AUTHORIZATION_CODE_EXPIRED(401, "A013", "Apple 인증 코드가 만료되었습니다."),
    APPLE_PUBLIC_KEY_GENERATION_FAILED(401, "A014", "Apple 공개키 생성에 실패하였습니다."),
    ACCESS_FORBIDDEN(403, "A015", "권한이 없습니다."),

    // 사용자 관련 예외
    MEMBER_NOT_FOUND(400, "B001", "회원을 찾지 못하였습니다."),
    MEMBER_EMAIL_DUPLICATED(409, "B002", "이미 사용중인 이메일입니다."),
    MEMBER_ALREADY_EXISTS(409, "B003", "이미 가입된 회원입니다."),
    MEMBER_ALREADY_LEFT(403, "B004", "탈퇴한 회원입니다."),
    SERVICE_TERMS_NOT_AGREED(403, "B005", "서비스 이용 약관에 동의 하지 않았습니다."),

    // 서비스 기능 관련 예외
    DISTANCE_OUT_OF_RANGE(400, "C001", "위치 정보가 오차를 벗어났습니다."),
    ARGUMENT_NOT_SATISFIED(400, "C002", "요청이 잘못되었습니다."),
    BOOKMARK_ALREADY_EXISTS(400, "C003", "해당 점포의 북마크가 이미 존재합니다."),
    REPORT_REASON_TEXT_REQUIRED(400, "C004", "기타 사유는 사유를 작성해야 합니다."),
    REVIEW_NOT_OWNED_BY_USER(403, "C005", "사용자가 작성한 리뷰가 아닙니다."),
    BOOKMARK_NOT_FOUND(400, "C006", "북마크를 찾지 못하였습니다."),
    STORE_NOT_FOUND(400, "C007", "점포를 찾지 못하였습니다."),
    REVIEW_NOT_FOUND(400, "C008", "리뷰를 찾지 못하였습니다."),
    RESOURCE_NOT_OWNED_BY_USER(403, "C009", "권한이 없습니다."),
    STORE_VISIT_CERT_NOT_AVAILABLE(400, "C010", "24시간 이내에 이미 방문 인증을 완료하였습니다."),
    BOOKMARKS_REGISTERED_BY_MEMBER_NOT_FOUND(400, "C011", "회원이 등록한 북마크가 없습니다."),
    STORES_REGISTERED_BY_MEMBER_NOT_FOUND(400, "C012", "회원이 등록한 점포가 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

}
