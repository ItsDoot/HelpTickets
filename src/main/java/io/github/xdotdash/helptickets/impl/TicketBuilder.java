package io.github.xdotdash.helptickets.impl;

import io.github.xdotdash.helptickets.api.Ticket;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TicketBuilder implements Ticket.Builder {

    private long id;
    private User creator;
    private String message;
    private Transform<World> transform;
    private LocalDateTime creationDate;
    private boolean completed = false;

    public TicketBuilder() {
    }

    @Override
    public Ticket.Builder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public Ticket.Builder creator(User creator) {
        this.creator = creator;
        return this;
    }

    @Override
    public Ticket.Builder message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Ticket.Builder transform(Transform<World> transform) {
        this.transform = transform;
        return this;
    }

    @Override
    public Ticket.Builder creationDate(@Nullable LocalDateTime date) {
        this.creationDate = date;
        return this;
    }

    @Override
    public Ticket.Builder completed(boolean completed) {
        this.completed = completed;
        return this;
    }

    @Override
    public Ticket build() {
        checkState(id > 0, "id cannot be less than 1");
        checkNotNull(creator, "creator cannot be null");
        checkNotNull(message, "message cannot be null");
        checkNotNull(transform, "transform cannot be null");

        return new TicketImpl(creator, id, message, transform, creationDate != null ? creationDate : LocalDateTime.now(), completed);
    }

    @Override
    public Ticket.Builder from(Ticket value) {
        id = value.getId();
        creator = value.getCreator();
        message = value.getMessage();
        transform = value.getTransform();
        creationDate = value.getCreationDate();
        completed = value.isCompleted();

        return this;
    }

    @Override
    public Ticket.Builder reset() {
        id = 0;
        creator = null;
        message = null;
        transform = null;
        creationDate = null;
        completed = false;

        return this;
    }
}
