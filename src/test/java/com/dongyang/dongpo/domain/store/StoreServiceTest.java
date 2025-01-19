package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.*;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import com.dongyang.dongpo.domain.store.repository.StoreOperatingDayRepository;
import com.dongyang.dongpo.domain.store.repository.StorePayMethodRepository;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.store.service.StoreReviewService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import com.dongyang.dongpo.domain.store.service.OpenPossibilityService;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.common.util.location.LocationUtil;
import com.dongyang.dongpo.common.util.member.MemberUtil;
import com.dongyang.dongpo.common.util.store.StoreUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreVisitCertRepository storeVisitCertRepository;

    @Mock
    private StorePayMethodRepository storePayMethodRepository;

    @Mock
    private StoreOperatingDayRepository storeOperatingDayRepository;

    @Mock
    private StoreVisitCertRepository visitCertRepository;

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreReviewService storeReviewService;

    @Mock
    private LocationUtil locationUtil;

    @Mock
    private MemberUtil memberUtil;

    @Mock
    private StoreUtil storeUtil;

    @Mock
    private OpenPossibilityService openPossibilityService;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private TitleService titleService;


    @Test
    @DisplayName("점포_등록")
    void addStore() {
        // given
        StoreRegisterDto storeDto = mock(StoreRegisterDto.class);
        Member member = mock(Member.class);
        Store store = mock(Store.class);

        when(locationUtil.verifyStoreRegistration(storeDto)).thenReturn(true);
        when(storeRepository.save(any())).thenReturn(store);
        when(storeDto.getPayMethods()).thenReturn(List.of(PayMethod.CASH, PayMethod.CARD));
        when(storeDto.getOperatingDays()).thenReturn(List.of(OperatingDay.MON, OperatingDay.TUE));

        // when
        storeService.addStore(storeDto, member);

        // then
        verify(storeRepository).save(any());
        verify(store, times(1)).addPayMethods(anyList());
        verify(store, times(1)).addOperatingDays(anyList());
    }

    @Test
    @DisplayName("점포_목록_조회")
    void findAll() {
        when(storeRepository.findAll()).thenReturn(List.of(mock(Store.class)));
        List<StoreDto> storeDtos = storeService.findAll();

        assertFalse(storeDtos.isEmpty());
        verify(storeRepository).findAll();
    }

    @Test
    void findStoresByCurrentLocation() {
    }

    @Test
    void getStoreDetailInfo() {
        // given
        Store store = mock(Store.class);
        Member member = mock(Member.class);
        Optional<Store> optionalStore = Optional.of(store);
        OpenPossibility openPossibility = mock(OpenPossibility.class);
        StoreDetailInfoResponseDto storeDetailInfoResponseDto = mock(StoreDetailInfoResponseDto.class);

        when(storeRepository.findById(any())).thenReturn(optionalStore);
        when(openPossibilityService.getOpenPossibility(any())).thenReturn(openPossibility);
        when(bookmarkService.isStoreBookmarkedByMember(any(), any())).thenReturn(true);
        when(bookmarkService.getBookmarkCountByStore(any())).thenReturn(1L);
        when(store.toDetailInfoResponse(any(), anyBoolean(), anyLong(), anyList())).thenReturn(storeDetailInfoResponseDto);

        // when
        storeService.getStoreDetailInfo(store.getId(), member);

        // then
        verify(storeRepository).findById(any());
    }

    @Test
    void deleteStore() {
    }

    @Test
    void updateInfoStore() {
    }

    @Test
    void findOne() {
    }

    @Test
    void getMyRegisteredStores() {
        // given
        Member member = mock(Member.class);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        MyRegisteredStoresResponseDto myRegisteredStoresResponseDto1 = mock(MyRegisteredStoresResponseDto.class);
        MyRegisteredStoresResponseDto myRegisteredStoresResponseDto2 = mock(MyRegisteredStoresResponseDto.class);

        when(storeRepository.findByMember(member)).thenReturn(List.of(store1, store2));
        when(store1.toMyRegisteredStoresResponse()).thenReturn(myRegisteredStoresResponseDto1);
        when(store2.toMyRegisteredStoresResponse()).thenReturn(myRegisteredStoresResponseDto2);

        // when
        List<MyRegisteredStoresResponseDto> myRegisteredStoresResponseDtos = storeService.getMyRegisteredStores(member);

        // then
        assertThat(myRegisteredStoresResponseDtos).hasSize(2);
        assertThat(myRegisteredStoresResponseDtos).contains(myRegisteredStoresResponseDto1, myRegisteredStoresResponseDto2);
        verify(storeRepository).findByMember(member);
        verify(store1).toMyRegisteredStoresResponse();
        verify(store2).toMyRegisteredStoresResponse();
    }

    @Test
    void getMyRegisteredStoreCount() {
        // given
        Member member = mock(Member.class);

        when(storeRepository.countByMember(member)).thenReturn(2L);

        // when
        Long count = storeService.getMyRegisteredStoreCount(member);

        // then
        assertThat(count).isEqualTo(2L);
        verify(storeRepository).countByMember(member);
    }

    @Test
    @DisplayName("연령대별_추천_점포_조회")
    void testRecommendStoreByAge() {
        // given
        Member member = mock(Member.class);
        when(member.getBirthyear()).thenReturn("2000");
        when(memberUtil.getAgeGroup(member.getBirthyear())).thenReturn("20");

        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        Store store3 = mock(Store.class);

        when(storeRepository.findStoresByMemberAgeWithMostVisits(anyInt(), any(Pageable.class)))
            .thenReturn(List.of(store1, store2, store3));

        // when
        RecommendResponse result = storeService.recommendStoreByAge(member);

        // then
        assertThat(result.getRecommendStores()).hasSize(3);
        assertThat(result.getRecommendationCategory()).isEqualTo("20");
        verify(storeRepository, times(1)).findStoresByMemberAgeWithMostVisits(anyInt(), any(Pageable.class));
    }

    @Test
    @DisplayName("성별_추천_점포_조회")
    void testRecommendStoreByGender() {
        // given
        Member member = mock(Member.class);
        when(member.getGender()).thenReturn(Member.Gender.GEN_MALE);

        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        Store store3 = mock(Store.class);

        when(storeRepository.findStoresByMemberGenderWithMostVisits(eq(Member.Gender.GEN_MALE), any(Pageable.class)))
            .thenReturn(List.of(store1, store2, store3));

        // when
        RecommendResponse result = storeService.recommendStoreByGender(member);

        // then
        assertThat(result.getRecommendStores()).hasSize(3);
        verify(storeRepository, times(1)).findStoresByMemberGenderWithMostVisits(eq(Member.Gender.GEN_MALE), any(Pageable.class));
    }
}