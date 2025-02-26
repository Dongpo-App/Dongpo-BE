package com.dongyang.dongpo.common.fileupload;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file-upload")
@Tag(name = "FileUpload API", description = "파일 업로드 API")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    // 사진 파일 업로드 공용 API
    @PostMapping
    public ResponseEntity<ApiResponse<List<UrlResponseDto>>> uploadFiles(@RequestParam("image") List<MultipartFile> images) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(fileUploadService.uploadFiles(images))); // return image Urls
    }
}
