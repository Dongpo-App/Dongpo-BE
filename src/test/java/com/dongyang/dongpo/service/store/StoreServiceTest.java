package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import com.dongyang.dongpo.dto.store.*;
import com.dongyang.dongpo.repository.store.StoreOperatingDayRepository;
import com.dongyang.dongpo.repository.store.StorePayMethodRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import com.dongyang.dongpo.service.open.OpenPossibilityService;
import com.dongyang.dongpo.service.title.TitleService;
import com.dongyang.dongpo.util.location.LocationUtil;
import com.dongyang.dongpo.util.member.MemberUtil;
import com.dongyang.dongpo.util.store.StoreUtil;
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
        StorePayMethod storePayMethod = mock(StorePayMethod.class);
        StoreOperatingDay storeOperatingDay = mock(StoreOperatingDay.class);

        when(locationUtil.verifyStoreRegistration(storeDto)).thenReturn(true);
        when(storeRepository.save(any())).thenReturn(store);
        when(storePayMethodRepository.save(any())).thenReturn(storePayMethod);
        when(storeOperatingDayRepository.save(any())).thenReturn(storeOperatingDay);
        when(storeDto.getPayMethods()).thenReturn(List.of(Store.PayMethod.CASH, Store.PayMethod.CARD));
        when(storeDto.getOperatingDays()).thenReturn(List.of(Store.OperatingDay.MON, Store.OperatingDay.TUE));

        // when
        storeService.addStore(storeDto, member);

        // then
        verify(storeRepository).save(any());
        verify(storePayMethodRepository, times(2)).save(any());
        verify(storeOperatingDayRepository, times(2)).save(any());
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
    void detailStore() {
        // given
        Store store = mock(Store.class);
        Optional<Store> optionalStore = Optional.of(store);
        StoreDetailsResponseDto storeDetailsResponseDto = mock(StoreDetailsResponseDto.class);

        when(storeRepository.findById(any())).thenReturn(optionalStore);
        when(bookmarkService.getBookmarkCountByStore(any())).thenReturn(1L);
        when(store.toDetailsResponse(anyLong())).thenReturn(storeDetailsResponseDto);

        // when
        storeService.detailStore(store.getId());

        // then
        verify(storeRepository).findById(any());
    }

    @Test
    void deleteStore() {
    }

    @Test
    void updateStore() {
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
        StoreSummaryResponseDto storeSummaryResponseDto1 = mock(StoreSummaryResponseDto.class);
        StoreSummaryResponseDto storeSummaryResponseDto2 = mock(StoreSummaryResponseDto.class);

        when(storeRepository.findByMember(member)).thenReturn(List.of(store1, store2));
        when(store1.toSummaryResponse()).thenReturn(storeSummaryResponseDto1);
        when(store2.toSummaryResponse()).thenReturn(storeSummaryResponseDto2);

        // when
        List<StoreSummaryResponseDto> storeSummaryResponseDtos = storeService.getMyRegisteredStores(member);

        // then
        assertThat(storeSummaryResponseDtos).hasSize(2);
        assertThat(storeSummaryResponseDtos).contains(storeSummaryResponseDto1, storeSummaryResponseDto2);
        verify(storeRepository).findByMember(member);
        verify(store1).toSummaryResponse();
        verify(store2).toSummaryResponse();
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

        when(storeRepository.findStoresByMemberAgeWithMostVisits(anyInt(), anyInt(), any(Pageable.class)))
            .thenReturn(List.of(store1, store2, store3));

        // when
        RecommendResponse result = storeService.recommendStoreByAge(member);

        // then
        assertThat(result.getRecommendStores()).hasSize(3);
        assertThat(result.getRecommendationCategory()).isEqualTo("20");
        verify(storeRepository, times(1)).findStoresByMemberAgeWithMostVisits(anyInt(),
			anyInt() ,any(Pageable.class));
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