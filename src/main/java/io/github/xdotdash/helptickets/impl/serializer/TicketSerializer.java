package io.github.xdotdash.helptickets.impl.serializer;

import com.google.common.reflect.TypeToken;
import io.github.xdotdash.helptickets.api.Ticket;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.World;

import java.time.LocalDateTime;

import static io.github.xdotdash.helptickets.impl.util.Conditional.checkNotNull;


public class TicketSerializer implements TypeSerializer<Ticket> {

    @Override
    public Ticket deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String id = value.getNode("id").toString();
        User creator = value.getNode("creator").getValue(TypeToken.of(User.class));
        String message = value.getNode("message").getString();
        Transform<World> transform = value.getNode("transform").getValue(new TypeToken<Transform<World>>() {});
        LocalDateTime creationDate = value.getNode("creationDate").getValue(TypeToken.of(LocalDateTime.class));
        boolean completed = value.getNode("completed").getBoolean(false);

        checkNotNull(id, new ObjectMappingException("id must not be virtual"));
        checkNotNull(creator, new ObjectMappingException("creator must not be virtual"));
        checkNotNull(message, new ObjectMappingException("message must not be virtual"));
        checkNotNull(transform, new ObjectMappingException("transform must not be virtual"));
        checkNotNull(creationDate, new ObjectMappingException("creationDate must not be virtual"));

        return Ticket.builder().id(id).creator(creator).message(message).transform(transform).creationDate(creationDate)
                .completed(completed).build();
    }

    @Override
    public void serialize(TypeToken<?> type, Ticket ticket, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("id").setValue(ticket.getId());
        value.getNode("creator").setValue(TypeToken.of(User.class), ticket.getCreator());
        value.getNode("message").setValue(ticket.getMessage());
        value.getNode("transform").setValue(new TypeToken<Transform<World>>() {}, ticket.getTransform());
        value.getNode("creationDate").setValue(TypeToken.of(LocalDateTime.class), ticket.getCreationDate());
        value.getNode("completed").setValue(ticket.isCompleted());
    }
}
