package io.github.xdotdash.helptickets.command;

import com.google.inject.Inject;
import io.github.xdotdash.helptickets.LangConfig;
import io.github.xdotdash.helptickets.api.Ticket;
import io.github.xdotdash.helptickets.api.command.AbstractCommand;
import io.github.xdotdash.helptickets.api.command.annotation.Command;
import io.github.xdotdash.helptickets.api.command.annotation.Permission;
import io.github.xdotdash.helptickets.api.command.annotation.PlayerOnly;
import io.github.xdotdash.helptickets.api.service.TicketService;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.time.LocalDateTime;

@Command({"create", "new"})
@Permission("helptickets.cmd.create")
@PlayerOnly
public class CommandTicketCreate extends AbstractCommand {

    private final TicketService service;

    @Inject
    public CommandTicketCreate(TicketService service) {
        this.service = service;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
        String message = ctx.<String>getOne("message").get();
        Player player = (Player) src;

        long id = generateTicketId();

        Ticket ticket = Ticket.builder()
                .id(id)
                .creator(player)
                .message(message)
                .transform(player.getTransform())
                .creationDate(LocalDateTime.now())
                .build();

        service.addTicket(ticket);

        player.sendMessage(TextSerializers.FORMATTING_CODE
                .deserialize(LangConfig.get("messages", "successfully-created").getString()
                        .replace("{id}", String.valueOf(id))));

        return CommandResult.success();
    }

    @Override
    public CommandElement args() {
        return GenericArguments.remainingJoinedStrings(Text.of("message"));
    }

    private long generateTicketId() throws CommandException {
        long maxId = service.getAllIds().stream()
                .max(Long::compare)
                .orElseThrow(() -> new CommandException(Text.of("Error while generating id for ticket")));

        return maxId + 1;
    }
}
