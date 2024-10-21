package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendResponse {

	private Long id;
	private String name;
	private String address;

	public static RecommendResponse fromAge(Store store) {
		return RecommendResponse.builder()
			.id(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.build();
	}

	public static RecommendResponse fromGender(Store store) {
		return RecommendResponse.builder()
			.id(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.build();
	}
}
