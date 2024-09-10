package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private StoreService storeService;

    private Claims claims = mock(Claims.class);

    @Test
    @DisplayName("점포_등록")
    void addStore() throws Exception {
        StoreDto storeDto = mock(StoreDto.class);

        when(jwtTokenProvider.parseClaims(any())).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@test.com");

        when(memberRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mock(Member.class)));
        when(storeRepository.save(any())).thenReturn(mock(Store.class));

        storeService.addStore(storeDto, any());

        verify(storeRepository).save(any());
    }

    @Test
    @DisplayName("점포_목록_조회")
    void findAll() {
        List<StoreDto> storeDtos = storeService.findAll();

        verify(storeRepository).findAll();
    }

    @Test
    void findStoresByCurrentLocation() {
    }

    @Test
    void detailStore() {
    }

    @Test
    void deleteStore() {
    }

    @Test
    void updateStore() {
    }

    @Test
    void myRegStore() {
    }

    @Test
    void findOne() {
    }
}