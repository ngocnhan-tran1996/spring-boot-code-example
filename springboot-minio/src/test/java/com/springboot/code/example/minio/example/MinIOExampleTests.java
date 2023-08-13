package com.springboot.code.example.minio.example;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.FileCopyUtils;
import com.springboot.code.example.minio.connection.MinIOServer;
import com.springboot.code.example.minio.constant.MinIOConstant;
import io.minio.GetObjectArgs;

@SpringBootTest
class MinIOExampleTests {

  @Autowired
  MinIOExample minIOExample;

  @Autowired
  MinIOServer minIOServer;

  @Test
  void testUploadFile() throws Exception {

    minIOExample.uploadFile();
    var minioClient = minIOServer.connect();
    try (var inputStream = minioClient.getObject(
        GetObjectArgs.builder()
            .bucket(MinIOConstant.MY_NAME.getValue())
            .object("image.jpg")
            .build())) {

      var output = new ByteArrayResource(FileCopyUtils.copyToByteArray(inputStream));
      assertThat(output.contentLength())
          .isEqualTo(1604321L);
    }

  }

}
