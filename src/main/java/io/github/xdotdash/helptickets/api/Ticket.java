package io.github.xdotdash.helptickets.api;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public interface Ticket {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    static Ticket complete(Ticket ticket) {
        return builder().from(ticket).completed(true).build();
    }

    long getId();

    User getCreator();

    String getMessage();

    Transform<World> getTransform();

    LocalDateTime getCreationDate();

    boolean isCompleted();

    interface Builder extends ResettableBuilder<Ticket, Builder> {

        Builder id(long id);

        Builder creator(User creator);

        Builder message(String message);

        Builder transform(Transform<World> transform);

        Builder creationDate(@Nullable LocalDateTime date);

        Builder completed(boolean completed);

        Ticket build();

    }

}
