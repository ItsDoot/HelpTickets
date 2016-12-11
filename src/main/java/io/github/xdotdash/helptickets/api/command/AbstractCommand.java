package io.github.xdotdash.helptickets.api.command;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.xdotdash.helptickets.api.command.annotation.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.serializer.TextSerializers;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractCommand implements CommandExecutor {

    @Override
    public abstract CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException;

    public CommandElement args() {
        return null;
    }

    public Injector childInjector() {
        return Guice.createInjector();
    }

    public final CommandSpec buildSpec() {
        Description description = getClass().getAnnotation(Description.class);
        Permission permission = getClass().getAnnotation(Permission.class);
        Children children = getClass().getAnnotation(Children.class);
        PlayerOnly playerOnly = getClass().getAnnotation(PlayerOnly.class);

        CommandSpec.Builder spec = CommandSpec.builder();

        if (args() != null) spec.arguments(args());

        if (permission != null) spec.permission(permission.value());

        if (description != null) spec.description(TextSerializers.FORMATTING_CODE.deserialize(description.value()));

        if (children != null) {
            Injector childInjector = childInjector();

            for (Class<? extends AbstractCommand> child : children.value()) {
                AbstractCommand cmd = childInjector.getInstance(AbstractCommand.class);
                spec.child(cmd.buildSpec(), cmd.getAliases());
            }
        }

        if (playerOnly != null) {
            spec.executor(new PlayerOnlyCommandExecutor(this));
        } else {
            spec.executor(this);
        }

        return spec.build();
    }

    public final String[] getAliases() {
        return checkNotNull(getClass().getAnnotation(Command.class), "Classes that extend AbstractCommand must be annotated with Command").value();
    }

    public final void register(Object plugin) {
        Sponge.getCommandManager().register(plugin, buildSpec(), getAliases());
    }
}