package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.dto.location.CoordinateRange;
import com.dongyang.dongpo.common.dto.location.LatLong;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.enums.Gender;
import com.dongyang.dongpo.domain.member.enums.Title;
import com.dongyang.dongpo.domain.store.dto.*;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.common.util.location.LocationUtil;
import com.dongyang.dongpo.common.util.member.MemberUtil;
import com.dongyang.dongpo.domain.store.service.OpenPossibilityServiceImpl;
import com.dongyang.dongpo.domain.store.service.StoreServiceImpl;
import com.dongyang.dongpo.domain.review.service.ReviewService;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
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
    private StoreVisitCertServiceImpl storeVisitCertService;

    @Mock
    private TitleService titleService;

    @Mock
    private OpenPossibilityServiceImpl openPossibilityService;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private LocationUtil locationUtil;

    @Mock
    private MemberUtil memberUtil;

    @InjectMocks
    private StoreServiceImpl storeService;

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
        // given
        LatLong latLong = new LatLong(37.5665, 126.9780);
        Member member = mock(Member.class);
        Store store = mock(Store.class);
        when(locationUtil.calcCoordinateRangeByCurrentLocation(latLong)).thenReturn(new CoordinateRange(37.0, 38.0, 126.0, 127.0));
        when(storeRepository.findStoresWithBookmarksWithinRange(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyLong()))
                .thenReturn(anyList());

        // when
        List<Object> result = Collections.singletonList(storeService.findStoresByCurrentLocation(latLong, member));

        // then
        assertThat(result).isNotEmpty();
        verify(storeRepository).findStoresWithBookmarksWithinRange(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyLong());
    }

    @Test
    void getStoreBasicInfo() {
        // given
        Store store = mock(Store.class);
        Member member = mock(Member.class);
        OpenPossibility openPossibility = mock(OpenPossibility.class);
        StoreBasicInfoResponseDto storeBasicInfoResponseDto = mock(StoreBasicInfoResponseDto.class);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(openPossibilityService.getOpenPossibility(any())).thenReturn(openPossibility);
        when(bookmarkService.isStoreBookmarkedByMember(any(), any())).thenReturn(true);
        when(reviewService.getLatestReviewPicsByStoreId(anyLong())).thenReturn(List.of("pic1", "pic2"));
        when(store.toBasicInfoResponse(any(), anyBoolean(), anyList())).thenReturn(storeBasicInfoResponseDto);

        // when
        StoreBasicInfoResponseDto result = storeService.getStoreBasicInfo(store.getId(), member);

        // then
        assertThat(result).isNotNull();
        verify(storeRepository).findById(any());
        verify(openPossibilityService).getOpenPossibility(any());
        verify(bookmarkService).isStoreBookmarkedByMember(any(), any());
        verify(reviewService).getLatestReviewPicsByStoreId(anyLong());
        verify(store).toBasicInfoResponse(any(), anyBoolean(), anyList());
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
        Store store = mock(Store.class);
        Member member = mock(Member.class);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(store.getMember()).thenReturn(member);
        when(member.getId()).thenReturn(1L);

        storeService.deleteStore(1L, member);

        verify(store).updateStoreStatusDeleted();
        verify(storeRepository).findById(any());
    }

    @Test
    void updateStoreInfo() {
        Store store = mock(Store.class);
        Member member = mock(Member.class);
        StoreInfoUpdateDto updateDto = mock(StoreInfoUpdateDto.class);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(store.getMember()).thenReturn(member);
        when(member.getId()).thenReturn(1L);

        storeService.updateStoreInfo(1L, updateDto, member);

        verify(store).updateInfo(updateDto);
        verify(storeRepository).findById(any());
    }

    @Test
    void getMyRegisteredStores() {
        // given
        Member member = mock(Member.class);
        PageRequest pageRequest = PageRequest.of(0, 20);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        MyRegisteredStoresResponseDto myRegisteredStoresResponseDto1 = mock(MyRegisteredStoresResponseDto.class);
        MyRegisteredStoresResponseDto myRegisteredStoresResponseDto2 = mock(MyRegisteredStoresResponseDto.class);
        Page<Store> myRegisteredStores = new PageImpl<>(List.of(store1, store2), pageRequest, 2);

        when(storeRepository.findByMemberAndPageRequest(member, pageRequest)).thenReturn(myRegisteredStores);
        when(store1.toMyRegisteredStoresResponse()).thenReturn(myRegisteredStoresResponseDto1);
        when(store2.toMyRegisteredStoresResponse()).thenReturn(myRegisteredStoresResponseDto2);

        // when
        Page<MyRegisteredStoresResponseDto> result = storeService.getMyRegisteredStores(member, 0);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(myRegisteredStoresResponseDto1, myRegisteredStoresResponseDto2);
        verify(storeRepository).findByMemberAndPageRequest(member, pageRequest);
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
    void findOne() {
        // given
        Store store = mock(Store.class);
        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(store.toResponse()).thenReturn(mock(StoreDto.class));

        // when
        StoreDto result = storeService.findOne(1L);

        // then
        assertThat(result).isNotNull();
        verify(storeRepository).findById(any());
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
        when(member.getGender()).thenReturn(Gender.GEN_MALE);

        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        Store store3 = mock(Store.class);

        when(storeRepository.findStoresByMemberGenderWithMostVisits(eq(Gender.GEN_MALE), any(Pageable.class)))
            .thenReturn(List.of(store1, store2, store3));

        // when
        RecommendResponse result = storeService.recommendStoreByGender(member);

        // then
        assertThat(result.getRecommendStores()).hasSize(3);
        verify(storeRepository, times(1)).findStoresByMemberGenderWithMostVisits(eq(Gender.GEN_MALE), any(Pageable.class));
    }

    @Test
    void visitCert() {
        // given
        StoreVisitCertDto storeVisitCertDto = mock(StoreVisitCertDto.class);
        Member member = mock(Member.class);
        Store store = mock(Store.class);
        LatLong latLong = new LatLong(37.5665, 126.9780);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(locationUtil.calcDistance(any(), any())).thenReturn(50L);
        when(storeVisitCertService.checkStoreVisitCertBy24Hours(any(), any())).thenReturn(false);
        when(storeVisitCertDto.getIsVisitSuccessful()).thenReturn(true);
        when(storeVisitCertService.getStoreVisitCertSuccessCount(any())).thenReturn(1L);

        // when
        storeService.visitCert(1L, storeVisitCertDto, member);

        // then
        verify(storeVisitCertService).addStoreVisitCert(any(), any(), eq(true));
        verify(titleService).addTitle(any(), eq(Title.FIRST_VISIT_CERT));
    }

    @Test
    void checkVisitCertBy24Hours() {
        // given
        Store store = mock(Store.class);
        Member member = mock(Member.class);

        when(storeRepository.findById(any())).thenReturn(Optional.of(store));
        when(storeVisitCertService.checkStoreVisitCertBy24Hours(any(), any())).thenReturn(true);

        // when
        Boolean result = storeService.checkVisitCertBy24Hours(1L, member);

        // then
        assertThat(result).isTrue();
        verify(storeVisitCertService).checkStoreVisitCertBy24Hours(any(), any());
    }
}