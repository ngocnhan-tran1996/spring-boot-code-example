package com.springboot.code.example.log4j2.rewrite;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Plugin(name = "MaskPattern", category = Node.CATEGORY, printObject = true)
public final class MaskPatternPlugin {

  private final String value;

  @PluginFactory
  public static MaskPatternPlugin createPatternPlugin(
      @PluginValue("value") final String value) {

    return new MaskPatternPlugin(value);
  }

  @Override
  public String toString() {
    return "Pattern =" + this.getValue();
  }
}
