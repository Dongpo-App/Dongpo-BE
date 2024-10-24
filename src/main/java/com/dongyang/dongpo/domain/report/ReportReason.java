package com.dongyang.dongpo.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportReason {

	// Review
	PROMOTIONAL_REVIEW("홍보성 리뷰입니다."),
	SPAM("도배글 입니다."),
	IRRELEVANT_CONTENT("가게에 무관한 내용입니다."),

	// Store
	WRONG_ADDRESS("주소가 잘못되었습니다."),
	NOT_EXIST_STORE("존재하지 않는 가게입니다."),


	INAPPOSITE_INFO("부적절한 정보가 포함되어 있습니다."),
	ETC("기타")
	;

	private final String reason;

}
