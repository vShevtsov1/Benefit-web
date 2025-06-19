package com.example.Redi.s3;

import com.example.Redi.s3.DTO.UploadResult;
import lombok.var;
import org.apache.poi.ss.usermodel.Workbook;
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

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.UUID;


public abstract class S3Service {

    private final S3Client s3Client;

    private final String bucketName;


    public S3Service(String bucketName, String keyID, String accessKey, String endpoint) {
        this.bucketName = bucketName;
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

    public UploadResult uploadExcel(String folder, Workbook workbook) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String filename = uuid + ".xlsx";
        File tempFile = File.createTempFile(uuid, ".xlsx");

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            workbook.write(out);
        }

        String key = folder + "/" + filename;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl(ObjectCannedACL.PRIVATE)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();

        PutObjectResponse response = s3Client.putObject(putRequest, RequestBody.fromFile(tempFile));

        Files.deleteIfExists(tempFile.toPath());

        String url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();

        return new UploadResult(key, url);
    }


}
