package com.springboot.code.example.minio.connection;

import org.springframework.stereotype.Service;
import com.springboot.code.example.minio.constant.MinIOConstant;
import io.minio.MinioClient;

@Service
public class MinIOServer {

  public MinioClient connect() {

    return MinioClient.builder()
        .endpoint("http://127.0.0.1", 9000, false)
        .credentials(MinIOConstant.MY_NAME.getValue(), MinIOConstant.MY_NAME.getValue())
        .build();
  }

}
