package com.dongyang.dongpo.domain.report;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportRequestDto;
import com.dongyang.dongpo.domain.report.dto.ReportResponseDto;
import com.dongyang.dongpo.domain.report.entity.Report;
import com.dongyang.dongpo.domain.report.enums.ReportReason;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import com.dongyang.dongpo.domain.report.repository.ReportRepository;
import com.dongyang.dongpo.domain.report.service.ReportServiceImpl;
import com.dongyang.dongpo.domain.review.entity.Review;
import com.dongyang.dongpo.domain.review.service.ReviewServiceImpl;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.service.StoreServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private StoreServiceImpl storeService;

    @Mock
    private ReviewServiceImpl reviewService;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    @DisplayName("신고 등록 성공 - 점포")
    void addReport_Store_Success() {
        ReportRequestDto request = mock(ReportRequestDto.class);
        Member member = mock(Member.class);
        Store store = mock(Store.class);

        when(request.getType()).thenReturn(ReportType.STORE);
        when(request.getReason()).thenReturn(ReportReason.NOT_EXIST_STORE);
        when(request.getTargetId()).thenReturn(1L);
        when(storeService.findById(1L)).thenReturn(store);

        reportService.addReport(request, member);

        verify(storeService).findById(1L);
        verify(reportRepository).save(request.toEntity(member));
    }

    @Test
    @DisplayName("신고 등록 실패 - 점포")
    void addReport_Store_Failure() {
        ReportRequestDto request = mock(ReportRequestDto.class);
        Member member = mock(Member.class);

        when(request.getType()).thenReturn(ReportType.STORE);
        when(request.getReason()).thenReturn(ReportReason.NOT_EXIST_STORE);
        when(request.getTargetId()).thenReturn(1L);
        when(storeService.findById(1L)).thenThrow(new CustomException(ErrorCode.STORE_NOT_FOUND));

        CustomException exception = assertThrows(CustomException.class, () -> reportService.addReport(request, member));

        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
        verify(storeService).findById(1L);
        verify(reportRepository, never()).save(any());
    }

    @Test
    @DisplayName("신고 등록 성공 - 리뷰")
    void addReport_Review_Success() {
        ReportRequestDto request = mock(ReportRequestDto.class);
        Member member = mock(Member.class);
        Review review = mock(Review.class);

        when(request.getType()).thenReturn(ReportType.REVIEW);
        when(request.getReason()).thenReturn(ReportReason.IRRELEVANT_CONTENT);
        when(request.getTargetId()).thenReturn(1L);
        when(reviewService.findReviewById(1L)).thenReturn(review);

        reportService.addReport(request, member);

        verify(reviewService).findReviewById(1L);
        verify(reportRepository).save(request.toEntity(member));
    }

    @Test
    @DisplayName("신고 등록 실패 - 리뷰")
    void addReport_Review_Failure() {
        ReportRequestDto request = mock(ReportRequestDto.class);
        Member member = mock(Member.class);

        when(request.getType()).thenReturn(ReportType.REVIEW);
        when(request.getReason()).thenReturn(ReportReason.IRRELEVANT_CONTENT);
        when(request.getTargetId()).thenReturn(1L);
        when(reviewService.findReviewById(1L)).thenThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        CustomException exception = assertThrows(CustomException.class, () -> reportService.addReport(request, member));

        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
        verify(reviewService).findReviewById(1L);
        verify(reportRepository, never()).save(any());
    }

    @Test
    @DisplayName("내가 등록한 신고 조회 - 성공")
    void getMyReports_Success() {
        Member member = mock(Member.class);
        ReportType type = ReportType.REVIEW;
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Report report1 = mock(Report.class);
        Report report2 = mock(Report.class);
        ReportResponseDto responseDto1 = mock(ReportResponseDto.class);
        ReportResponseDto responseDto2 = mock(ReportResponseDto.class);
        Page<Report> reports = new PageImpl<>(List.of(report1, report2), pageRequest, 2);

        when(reportRepository.findByMemberAndType(member, type, pageRequest)).thenReturn(reports);
        when(report1.toResponseDto()).thenReturn(responseDto1);
        when(report2.toResponseDto()).thenReturn(responseDto2);

        Page<ReportResponseDto> result = reportService.getMyReports(member, type, page);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(responseDto1, responseDto2);
        verify(reportRepository).findByMemberAndType(member, type, pageRequest);
    }

    @Test
    @DisplayName("내가 등록한 신고 조회 - 실패")
    void getMyReports_Failure() {
        Member member = mock(Member.class);
        ReportType type = ReportType.REVIEW;
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Report> emptyReports = Page.empty(pageRequest);

        when(reportRepository.findByMemberAndType(member, type, pageRequest)).thenReturn(emptyReports);

        CustomException exception = assertThrows(CustomException.class, () -> reportService.getMyReports(member, type, page));

        assertEquals(ErrorCode.REPORTS_REGISTERED_BY_MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(reportRepository).findByMemberAndType(member, type, pageRequest);
    }

    @Test
    @DisplayName("신고 전체 조회 - 리뷰")
    void findAllReviewReport_Success() {
        Report report1 = mock(Report.class);
        Report report2 = mock(Report.class);

        when(reportRepository.findByType(ReportType.REVIEW)).thenReturn(List.of(report1, report2));

        List<Report> result = reportService.findAllReviewReport();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(report1, report2);
        verify(reportRepository).findByType(ReportType.REVIEW);
    }

    @Test
    @DisplayName("특정 신고 조회 - 리뷰")
    void findOneReviewReport_Success() {
        Long reportId = 1L;
        Report report = mock(Report.class);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Report result = reportService.findOneReviewReport(reportId);

        assertThat(result).isEqualTo(report);
        verify(reportRepository).findById(reportId);
    }

    @Test
    @DisplayName("신고 전체 조회 - 점포")
    void findAllStoreReport_Success() {
        Report report1 = mock(Report.class);
        Report report2 = mock(Report.class);

        when(reportRepository.findByType(ReportType.STORE)).thenReturn(List.of(report1, report2));

        List<Report> result = reportService.findAllStoreReport();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(report1, report2);
        verify(reportRepository).findByType(ReportType.STORE);
    }

    @Test
    @DisplayName("특정 신고 조회 - 점포")
    void findOneStoreReport_Success() {
        Long reportId = 1L;
        Report report = mock(Report.class);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Report result = reportService.findOneStoreReport(reportId);

        assertThat(result).isEqualTo(report);
        verify(reportRepository).findById(reportId);
    }
}
