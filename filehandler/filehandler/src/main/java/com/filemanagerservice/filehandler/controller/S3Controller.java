package com.filemanagerservice.filehandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.filemanagerservice.filehandler.service.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class S3Controller {
    @Autowired
    S3ServiceImp s3ServiceImp;

    // ✅ Download
    @GetMapping("/download-existing")
    public ResponseEntity<byte[]> downloadExisting() {
        byte[] data = s3ServiceImp.downloadFile("Dark_Wide.jpg");
        return ResponseEntity.ok(data);
    }

    // ✅ Upload
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file, Long userId) throws IOException {
        return ResponseEntity.ok(s3ServiceImp.uploadFile(file,userId));
    }

//    // ✅ View in browser (best way)
//    @GetMapping("/view")
//    public ResponseEntity<String> view(@RequestParam String key) {
//        return ResponseEntity.ok(s3ServiceImp.generatePresignedUrl(key));
//    }
//
//    // ✅ Test your existing file
//    @GetMapping("/existing")
//    public ResponseEntity<String> existing() {
//        return ResponseEntity.ok(s3ServiceImp.generatePresignedUrl("Dark_Wide.jpg"));
//    }
}
