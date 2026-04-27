package com.filemanagerservice.filehandler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import com.filemanagerservice.filehandler.model.*;
import com.filemanagerservice.filehandler.repository.*;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class S3ServiceImp {
    @Autowired
    private S3Client s3Client;
    private final String bucketName = "rahuls3bucket2026";

    @Autowired
    FileRepository fileRepository;

    public String uploadFile(MultipartFile file, Long userId) throws IOException {

        String key = userId + "/" +
                LocalDate.now() + "/" +
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        // upload to S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        // save in DB
        FileEntity entity = new FileEntity();
        entity.setUserId(userId);
        entity.setS3Key(key);
        entity.setFileName(file.getOriginalFilename());
        entity.setOriginalFileName(file.getOriginalFilename());
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());

        fileRepository.save(entity);
        return key;
    }

    public byte[] downloadFile(String key) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObjectAsBytes(request).asByteArray();
    }

}
