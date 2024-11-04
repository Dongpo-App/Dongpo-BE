package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendResponse {
	private String recommendationCategory;
	private List<RecommendStoresResponse> recommendStores;

	@Getter
	@Builder
	@JsonSerialize
	@JsonDeserialize
	public static class RecommendStoresResponse {
		private Long id;
		private String name;
		private String address;
	}

	public static RecommendResponse fromAge(List<Store> stores, String ageGroup) {
		return RecommendResponse.builder()
			.recommendationCategory(ageGroup)
				.recommendStores(stores.stream()
					.map(store -> RecommendStoresResponse.builder()
						.id(store.getId())
						.name(store.getName())
						.address(store.getAddress())
						.build())
					.toList())
			.build();
	}

	public static RecommendResponse fromGender(List<Store> stores, Member.Gender gender) {
		return RecommendResponse.builder()
			.recommendationCategory(gender.toString())
			.recommendStores(stores.stream()
				.map(store -> RecommendStoresResponse.builder()
					.id(store.getId())
					.name(store.getName())
					.address(store.getAddress())
					.build())
				.toList())
			.build();
	}
}
