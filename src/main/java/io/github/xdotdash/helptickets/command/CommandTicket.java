package io.github.xdotdash.helptickets.command;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.xdotdash.helptickets.api.command.AbstractCommand;
import io.github.xdotdash.helptickets.api.command.annotation.Children;
import io.github.xdotdash.helptickets.api.command.annotation.Command;
import io.github.xdotdash.helptickets.api.command.annotation.Description;
import io.github.xdotdash.helptickets.api.command.annotation.Permission;
import io.github.xdotdash.helptickets.api.service.TicketService;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.DARK_GRAY;
import static org.spongepowered.api.text.format.TextColors.GOLD;
import static org.spongepowered.api.text.format.TextColors.GREEN;

@Command({"ticket"})
@Permission("helptickets.use")
@Description("Base command of our ticketing system")
@Children({CommandTicketCreate.class})
public class CommandTicket extends AbstractCommand {

    private final TicketService service;

    public CommandTicket(TicketService ticketService) {
        this.service = ticketService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
        List<Text> commands = getCommandList(src).stream()
                .map(command -> Text.of(GREEN, command))
                .collect(Collectors.toList());

        if (commands.size() == 0) {
            throw new CommandPermissionException();
        }

        PaginationList page = PaginationList.builder()
                .title(Text.of(GOLD, "HelpTickets Commands"))
                .padding(Text.of(DARK_GRAY, "="))
                .linesPerPage(6)
                .contents(commands)
                .build();

        page.sendTo(src);

        return CommandResult.success();
    }

    @Override
    public Injector childInjector() {
        return Guice.createInjector(new TicketServiceGuiceModule(service));
    }

    private List<String> getCommandList(CommandSource src) {
        List<String> commands = new ArrayList<>();

        String permPrefix = "helptickets.cmd.";

        if (src.hasPermission(permPrefix + "create")) commands.add("- /ticket create <message>");
        if (src.hasPermission(permPrefix + "list")) commands.add("- /ticket list [player]");
        if (src.hasPermission(permPrefix + "tp")) commands.add("- /ticket tp <id>");
        if (src.hasPermission(permPrefix + "complete")) commands.add("- /ticket complete <id>");
        if (src.hasPermission(permPrefix + "delete")) commands.add("- /ticket delete <id>");
        if (src.hasPermission(permPrefix + "info")) commands.add("- /ticket info <id>");

        return commands;
    }

    class TicketServiceGuiceModule extends AbstractModule {

        private final TicketService ticketService;

        public TicketServiceGuiceModule(TicketService ticketService) {
            this.ticketService = ticketService;
        }

        @Override
        protected void configure() {
            bind(TicketService.class).toInstance(ticketService);
        }
    }

}
