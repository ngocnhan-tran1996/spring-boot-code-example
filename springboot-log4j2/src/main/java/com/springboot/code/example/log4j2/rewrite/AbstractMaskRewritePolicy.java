package com.springboot.code.example.log4j2.rewrite;

import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;

public abstract class AbstractMaskRewritePolicy implements RewritePolicy {

  protected abstract void mask(StringBuilder stringBuilder);

  protected abstract char getMaskCharacter();

}