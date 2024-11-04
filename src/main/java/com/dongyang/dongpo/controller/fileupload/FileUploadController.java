package com.dongyang.dongpo.controller.fileupload;

import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.fileupload.UrlResponseDto;
import com.dongyang.dongpo.service.fileupload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/file-upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    // 사진 파일 업로드 공용 API
    @PostMapping
    public ResponseEntity<ApiResponse<List<UrlResponseDto>>> uploadFiles(@RequestParam("image") List<MultipartFile> images) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(fileUploadService.uploadFiles(images))); // return image Urls
    }
}
