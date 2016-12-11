package io.github.xdotdash.helptickets.api.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.asset.Asset;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private final Logger logger;
    private final String filename;

    private final HoconConfigurationLoader loader;
    private CommentedConfigurationNode node;

    public Config(Logger logger, Path dir, String filename, Asset def) {
        this.logger = logger;
        this.filename = filename;

        Path file = dir.resolve(filename);

        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            logger.error("Error while creating directories for '" + filename + "'", e);
        }

        if (!file.toFile().exists()) {
            if (def != null) {
                try {
                    def.copyToFile(file);
                } catch (IOException e) {
                    logger.error("Error while copying default file for '" + filename + "' to directory", e);
                }
            } else {
                try {
                    file.toFile().createNewFile();
                } catch (IOException e) {
                    logger.error("Error while creating file for '" + filename + "'", e);
                }
            }
        }

        loader = HoconConfigurationLoader.builder().setPath(file).build();

        try {
            node = loader.load();
        } catch (IOException e) {
            logger.error("Error while loading configuration for '" + filename + "'", e);
        }
    }

    public void reload() {
        try {
            node = loader.load();
        } catch (IOException e) {
            logger.error("Error while reloading configuration for '" + filename + "'", e);
        }
    }

    public void save() {
        try {
            loader.save(node);
        } catch (IOException e) {
            logger.error("Error while saving configuration for '" + filename + "'", e);
        }
    }

    public CommentedConfigurationNode get(@Nonnull Object... path) {
        return node.getNode(path);
    }
    public CommentedConfigurationNode get() {
        return node;
    }

}
