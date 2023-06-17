package com.springboot.code.example.transaction.rabbitmq.datasource.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueueNames {

  private static final String OUT_PREFIX = "out_";
  private static final String TRANSACTION = "transaction";
  private static final String PRODUCER_SUFFIX = "_producer";
  private static final String CONSUMER_SUFFIX = "_consumer";
  private static final String SAME_SUFFIX = "_same";
  private static final String RELATIVE_SUFFIX = "_same";

  public static final String TRANSACTION_PRODUCER = TRANSACTION + PRODUCER_SUFFIX;
  public static final String TRANSACTION_CONSUMER = TRANSACTION + CONSUMER_SUFFIX;
  public static final String TRANSACTION_SAME_PRODUCER = TRANSACTION + SAME_SUFFIX
      + PRODUCER_SUFFIX;
  public static final String TRANSACTION_SAME_CONSUMER = TRANSACTION + SAME_SUFFIX
      + CONSUMER_SUFFIX;
  public static final String TRANSACTION_RELATIVE_PRODUCER = TRANSACTION + SAME_SUFFIX
      + RELATIVE_SUFFIX + PRODUCER_SUFFIX;
  public static final String TRANSACTION_RELATIVE_CONSUMER = TRANSACTION + SAME_SUFFIX
      + RELATIVE_SUFFIX + CONSUMER_SUFFIX;
  public static final String OUT_TRANSACTION_PRODUCER = OUT_PREFIX + TRANSACTION + PRODUCER_SUFFIX;
  public static final String OUT_TRANSACTION_CONSUMER = OUT_PREFIX + TRANSACTION + CONSUMER_SUFFIX;

}
