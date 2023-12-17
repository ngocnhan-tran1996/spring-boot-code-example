package com.springboot.code.example.aspect;

import org.springframework.stereotype.Service;

@Service
class AspectService {

  <T extends Throwable> void throwRuntime(T ex) throws T {

    throw ex;
  }

}