package io.github.xdotdash.helptickets;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.github.xdotdash.helptickets.api.Ticket;
import io.github.xdotdash.helptickets.api.config.Config;
import io.github.xdotdash.helptickets.api.service.TicketService;
import io.github.xdotdash.helptickets.command.CommandTicket;
import io.github.xdotdash.helptickets.impl.TicketBuilder;
import io.github.xdotdash.helptickets.impl.serializer.LocalDateTimeSerializer;
import io.github.xdotdash.helptickets.impl.serializer.TicketSerializer;
import io.github.xdotdash.helptickets.impl.serializer.TransformWorldSerializer;
import io.github.xdotdash.helptickets.impl.serializer.UserSerializer;
import io.github.xdotdash.helptickets.impl.service.ConfigTicketService;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.World;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Plugin(id = "helptickets", name = "HelpTickets", version = "1.0-SNAPSHOT", description = "Player management made easy!",
        url = "https://github.com/xDotDash/HelpTickets", authors = {"DotDash"})
public class HelpTickets {

    public final Game game;
    public final Logger logger;
    public final Path configDir;

    @Inject
    public HelpTickets(Game game, Logger logger, @ConfigDir(sharedRoot = false) Path configDir) {
        this.game = game;
        this.logger = logger;
        this.configDir = configDir;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        registerBuilders();
        registerSerializers();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        registerServices();
        registerCommands();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    private void registerSerializers() {
        TypeSerializers.getDefaultSerializers()
                .registerType(TypeToken.of(LocalDateTime.class), new LocalDateTimeSerializer())
                .registerType(TypeToken.of(User.class),
                        new UserSerializer(game.getServiceManager().provideUnchecked(UserStorageService.class)))
                .registerType(new TypeToken<Transform<World>>() {}, new TransformWorldSerializer())
                .registerType(TypeToken.of(Ticket.class), new TicketSerializer());
    }

    private void registerBuilders() {
        game.getRegistry().registerBuilderSupplier(Ticket.Builder.class, TicketBuilder::new);
    }

    private void registerServices() {
        Config tickets = new Config(logger, configDir, "tickets.conf", null);

        game.getServiceManager().setProvider(this, TicketService.class, new ConfigTicketService(tickets));
    }

    private void registerCommands() {
        new CommandTicket(game.getServiceManager().provideUnchecked(TicketService.class)).register(this);
    }

}
