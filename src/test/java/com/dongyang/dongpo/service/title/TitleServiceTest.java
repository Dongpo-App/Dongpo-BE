package com.dongyang.dongpo.service.title;

import com.dongyang.dongpo.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TitleService titleService;

    @Test
    void addTitle() {

    }
}