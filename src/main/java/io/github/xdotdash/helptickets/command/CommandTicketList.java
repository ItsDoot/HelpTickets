package io.github.xdotdash.helptickets.command;

import io.github.xdotdash.helptickets.api.command.AbstractCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class CommandTicketList extends AbstractCommand {

    @Override
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
        Optional<User> user = ctx.getOne("player");

        return CommandResult.success();
    }

    @Override
    public CommandElement args() {
        return GenericArguments.optional(GenericArguments.user(Text.of("player")));
    }

}
