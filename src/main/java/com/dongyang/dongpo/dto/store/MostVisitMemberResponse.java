package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Title;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MostVisitMemberResponse {
	private Long id;
	private String nickname;
	private String profilePic;
	private String mainTitle;

	public static MostVisitMemberResponse of(Long id, String nickname, Title title, String profilePic) {
		return MostVisitMemberResponse.builder()
			.id(id)
			.nickname(nickname)
			.profilePic(profilePic)
			.mainTitle(title.getDescription())
			.build();
	}
}
