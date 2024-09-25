package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreIndexDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.bookmark.BookmarkRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final MemberRepository memberRepository;
    private final MemberTitleRepository memberTitleRepository;
    private final S3Service s3Service;
    private final StoreRepository storeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final StoreReviewRepository storeReviewRepository;

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    public MyPageDto getMyPageIndex(Member member) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);
        Long storeRegisterCount = storeRepository.countByMember(member);
        return MyPageDto.toEntity(member, memberTitles, storeRegisterCount);
    }

    public List<StoreIndexDto> getMyRegisteredStores(Member member) {
        List<StoreIndexDto> storeIndexDtos = new ArrayList<>();
        storeRepository.findByMember(member).forEach(store -> {
            storeIndexDtos.add(store.toIndexResponse());
        });
        return storeIndexDtos;
    }

    public List<MyPageDto.TitleDto> getMyTitles(Member member) {
        List<MyPageDto.TitleDto> titleDtos = new ArrayList<>();
        memberTitleRepository.findByMember(member).forEach(memberTitle -> {
            titleDtos.add(MyPageDto.toTitleDto(memberTitle));
        });
        return titleDtos;
    }

    public List<BookmarkDto> getMyBookmarks(Member member) {
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();
        bookmarkRepository.findByMember(member).forEach(bookmark -> {
            bookmarkDtos.add(BookmarkDto.toDto(bookmark));
        });
        return bookmarkDtos;
    }

    public List<ReviewDto> getMyReviews(Member member) {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        storeReviewRepository.findByMember(member).forEach(storeReview -> {
            reviewDtos.add(ReviewDto.toDto(storeReview));
        });
        return reviewDtos;
    }

    @Transactional
    public void updateMyPageInfo(String email, MyPageUpdateDto myPageUpdateDto) {
        if (myPageUpdateDto.getNickname().length() > 7) // 문자 수 7자 초과시 예외 발생
            throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);

        memberRepository.findByEmail(email).ifPresent(member -> {
            if (myPageUpdateDto.getProfilePic() != null && !myPageUpdateDto.getProfilePic().isBlank()) {
                if (member.getProfilePic() != null && member.getProfilePic().startsWith(bucketFullUrl))
                    s3Service.deleteFile(member.getProfilePic()); // S3에 있는 기존 프로필 사진 삭제
                member.setProfilePic(myPageUpdateDto.getProfilePic());
                log.info("Updated Member profilePic : {}", member.getEmail());
            }
            if (myPageUpdateDto.getNickname() != null && !myPageUpdateDto.getNickname().equals(member.getNickname())) {
                member.setNickname(myPageUpdateDto.getNickname());
                log.info("Updated Member nickname : {}", member.getEmail());
            }
            if (myPageUpdateDto.getNewMainTitle() != null && !myPageUpdateDto.getNewMainTitle().equals(member.getMainTitle())) {
                MemberTitle memberTitle = memberTitleRepository.findByMemberAndTitle(member, myPageUpdateDto.getNewMainTitle());
                member.setMainTitle(memberTitle.getTitle());
                log.info("Updated Member mainTitle : {}", member.getEmail());
            }
        });
    }
}
