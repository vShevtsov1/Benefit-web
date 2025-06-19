package com.example.Redi.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportS3Service extends S3Service {

    @Autowired
    public ReportS3Service(@Value("${bucket.name.report}") String bucketName,
                           @Value("${minio.access.key}") String keyID,
                           @Value("${minio.secret.key}") String accessKey,
                           @Value("${minio.endpoint}") String endpoint) {
        super(bucketName, keyID, accessKey, endpoint);
    }
}
