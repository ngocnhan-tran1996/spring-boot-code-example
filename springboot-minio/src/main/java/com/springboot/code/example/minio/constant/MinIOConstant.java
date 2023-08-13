package com.springboot.code.example.minio.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MinIOConstant {

  MY_NAME("ngocnhan");

  private final String value;

}