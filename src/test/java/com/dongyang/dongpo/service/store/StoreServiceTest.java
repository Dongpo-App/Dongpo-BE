package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.store.AddStoreRequest;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StoreService.class)
public class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private OperatingDayRepository operatingDayRepository;

    @MockBean
    private PayMethodRepository payMethodRepository;

    @Test
    public void testAddStore() throws Exception {
        // given
        String accessToken = "access-token";
        String email = "user@example.com";
        Member member = Member.builder()
                .email(email)
                .build();
        AddStoreRequest request = new AddStoreRequest();
        request.setName("test");
        request.setLocation("location");
        request.setCloseTime(LocalTime.parse("08:00"));
        request.setOpenTime(LocalTime.parse("20:00"));
        request.setPayMethods(Arrays.asList(new StorePayMethod.PayMethod[1]));
        request.setOperatingDays(Arrays.asList(new StoreOperatingDay.OperatingDay[1]));

        when(jwtTokenProvider.parseClaims(accessToken)).thenReturn(Claims.builder().setSubject(email).build());
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // when
        ResponseEntity response = storeService.addStore(request, accessToken);

        // then
        verify(storeRepository, times(1)).save(any(Store.class));
        verify(operatingDayRepository, times(1)).saveAll(anyList());
        verify(payMethodRepository, times(1)).saveAll(anyList());
        assertEquals(ResponseEntity.ok().build(), response);
    }
}
