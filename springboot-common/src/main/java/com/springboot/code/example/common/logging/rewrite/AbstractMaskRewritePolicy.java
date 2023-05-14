package com.springboot.code.example.common.logging.rewrite;

import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;

public abstract class AbstractMaskRewritePolicy implements RewritePolicy {

  protected abstract void mask(StringBuilder stringBuilder);

  protected abstract char getMaskCharacter();

}