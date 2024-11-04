package com.dongyang.dongpo.service.fileupload;

import com.dongyang.dongpo.dto.fileupload.UrlResponseDto;
import com.dongyang.dongpo.util.s3.S3Service;
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
public class FileUploadService {

    private final S3Service s3Service;

    @Transactional
    public List<UrlResponseDto> uploadFiles(List<MultipartFile> images) throws IOException {
        List<UrlResponseDto> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            imageUrls.add(UrlResponseDto.builder()
                    .imageUrl(s3Service.uploadFile(image))
                    .build());
        }
        return imageUrls;
    }

}
