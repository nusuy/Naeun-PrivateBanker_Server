package com.naeun.naeun_server.global.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class GcsUtil {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    public String uploadFile(MultipartFile file, String fileName, String ext) throws IOException {
        String keyFileName = "bucket-key.json";
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(ext).build();
        storage.createFrom(blobInfo, file.getInputStream());

        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
