package com.springboot.code.example.minio.example;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.springboot.code.example.minio.connection.MinIOServer;
import com.springboot.code.example.minio.constant.MinIOConstant;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MinIOExample {

  private final MinIOServer minIOServer;

  public void uploadFile() {

    try {

      var minioClient = minIOServer.connect();

      boolean found = minioClient.bucketExists(
          BucketExistsArgs
              .builder()
              .bucket(MinIOConstant.MY_NAME.getValue())
              .build());
      if (found) {

        log.info("Bucket '{}' already exists.", MinIOConstant.MY_NAME.getValue());
      } else {

        minioClient.makeBucket(
            MakeBucketArgs
                .builder()
                .bucket(MinIOConstant.MY_NAME.getValue())
                .build());
      }

      var image = new ClassPathResource("image.jpg");

      // Create a InputStream for object upload.
      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(MinIOConstant.MY_NAME.getValue())
              .object("image.jpg")
              .stream(
                  image.getInputStream(),
                  image.contentLength(),
                  -1)
              .build());

      log.info("Image is uploaded successfully");
    } catch (Exception e) {

      log.error("Error occurred: " + e);
    }

  }

}
