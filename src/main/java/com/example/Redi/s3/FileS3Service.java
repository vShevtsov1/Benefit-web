package com.example.Redi.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileS3Service extends S3Service {

    @Autowired
    public FileS3Service(@Value("${bucket.name}") String bucketName,
                         @Value("${minio.access.key}") String keyID,
                         @Value("${minio.secret.key}") String accessKey,
                         @Value("${minio.endpoint}") String endpoint) {
        super(bucketName, keyID, accessKey, endpoint);
    }


}
