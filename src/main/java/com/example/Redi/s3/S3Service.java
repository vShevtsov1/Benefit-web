package com.example.Redi.s3;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    private String bucketName;

    private String keyID;

    private String accessKey;

    @Autowired
    public S3Service(@Value("${bucket.name}") String bucketName,
                     @Value("${minio.access.key}") String keyID,
                     @Value("${minio.secret.key}") String accessKey,
                     @Value("${minio.endpoint}") String endpoint) {
        this.bucketName = bucketName;
        this.keyID = keyID;
        this.accessKey = accessKey;
        AwsBasicCredentials credentials = AwsBasicCredentials.create(keyID, accessKey);
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> credentials)
                .build();
    }

    public String uploadPhoto(String folder, MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = uuid + fileExtension;
        Path tempFile = Files.createTempFile(uuid, fileExtension);

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            String key = folder + "/" + filename;

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl(ObjectCannedACL.PRIVATE)
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(putRequest, RequestBody.fromFile(tempFile));
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
