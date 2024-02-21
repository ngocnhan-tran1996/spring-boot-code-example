package io.ngocnhan_tran1996.code.example.logger.rewrite;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Plugin(name = "MaskPattern", category = Node.CATEGORY, printObject = true)
final class MaskPatternPlugin {

    private final String value;

    @PluginFactory
    public static MaskPatternPlugin createPatternPlugin(
        @PluginValue("value") final String value) {

        return new MaskPatternPlugin(value);
    }

    @Override
    public String toString() {
        return "Pattern = " + this.getValue();
    }

}