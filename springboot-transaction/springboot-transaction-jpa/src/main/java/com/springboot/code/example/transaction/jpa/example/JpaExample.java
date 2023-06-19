package com.springboot.code.example.transaction.jpa.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.jpa.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaExample {

  private final HistoryRepository historyRepository;

  public int insertHistory() {

    return historyRepository.insertHistory("test");
  }

  public int insertHistoryWithoutTransaction() {

    return historyRepository.insertHistoryModifying("testModify");
  }

  @Transactional
  public int insertHistoryWithTransaction() {

    return historyRepository.insertHistoryModifying("testModify");
  }

}