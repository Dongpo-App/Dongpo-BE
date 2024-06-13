package com.dongyang.dongpo.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile imageFile) throws IOException {

        // 첨부파일(이미지가 없을 경우)
        if (imageFile == null || imageFile.isEmpty()) return "";

        // UUID로 파일 이름을 생성해 업로드
        String fileName = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());
        metadata.setContentType("image/jpeg");

        amazonS3.putObject(bucket, fileName, imageFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String fileName){

        // 첨부파일(이미지가 없을 경우)
        if (fileName == null || fileName.isBlank()) return;

        int index = fileName.indexOf(".com/");
        String file = fileName;

        if (index != -1) {
            file = fileName.substring(index + 5);
        }
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, file);
        amazonS3.deleteObject(request);

    }
}
