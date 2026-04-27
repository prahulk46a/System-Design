package com.filemanagerservice.filehandler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    //Step 1. Create an S3 client bean to interact with AWS S3
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // change if needed
                .credentialsProvider(ProfileCredentialsProvider.create("Girish_aws"))
                .build();
    }
}
