package com.dongyang.dongpo.domain.admin.service;

import com.dongyang.dongpo.common.fileupload.s3.S3Service;
import com.dongyang.dongpo.domain.admin.dto.NoticeDto;
import com.dongyang.dongpo.domain.admin.dto.PicDto;
import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.admin.repository.NoticePicRepository;
import com.dongyang.dongpo.domain.admin.repository.NoticeRepository;
import com.dongyang.dongpo.domain.board.Notice;
import com.dongyang.dongpo.domain.board.NoticePic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticePicRepository noticePicRepository;
    private final S3Service s3Service;

    public List<NoticeDto> findAll(){
        List<Notice> notices =  noticeRepository.findAll();
        List<NoticeDto> noticeDtos = new ArrayList<>();
        for (Notice notice : notices)
            noticeDtos.add(notice.toResponse());

        return noticeDtos;
    }

    @Transactional
    public void addNotice(NoticeDto noticeDto, Admin principal, List<MultipartFile> images) throws IOException {
        Notice notice = noticeRepository.save(noticeDto.toEntity(principal));

        for (MultipartFile image : images) {
            String imageUrl = s3Service.uploadFile(image);

            NoticePic noticePic = NoticePic.builder()
                    .notice(notice)
                    .picUrl(imageUrl)
                    .build();

            noticePicRepository.save(noticePic);
        }


        log.info("Add Notice ID : {} by Admin ID : {}", notice.getId(), principal.getId());
    }

    public NoticeDto detail(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        NoticeDto noticeDto  = notice.toResponse();

        List<NoticePic> noticePics = noticePicRepository.findByNoticeId(id);
        List<PicDto> picDtos = new ArrayList<>();

        for (NoticePic noticePic : noticePics)
            picDtos.add(noticePic.toResponse());

        noticeDto.setImgs(picDtos);

        return noticeDto;
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        NoticePic noticePic = noticePicRepository.findById(notice.getId()).orElse(null);

        noticePicRepository.delete(noticePic);
        noticeRepository.delete(notice);
    }
}
