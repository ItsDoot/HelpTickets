package io.github.xdotdash.helptickets;

import io.github.xdotdash.helptickets.api.config.Config;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import static com.google.common.base.Preconditions.checkState;

public class LangConfig {

    private static Config lang;

    static void init(Config lang) {
        checkState(LangConfig.lang == null, "LangConfig has already been initialized");

        LangConfig.lang = lang;
    }

    public static CommentedConfigurationNode get(Object... path) {
        return lang.get(path);
    }

    public static void reload() {
        lang.reload();
    }

    public static void save() {
        lang.save();
    }

}
