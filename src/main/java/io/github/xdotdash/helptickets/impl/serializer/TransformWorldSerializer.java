package io.github.xdotdash.helptickets.impl.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

public class TransformWorldSerializer implements TypeSerializer<Transform<World>> {

    static {
        TypeSerializers.getDefaultSerializers().registerType(new TypeToken<Transform<World>>() {},
                new TransformWorldSerializer());
    }

    @Override
    public Transform<World> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String worldStr = value.getNode("world").getString();

        World world = Sponge.getServer().getWorld(worldStr)
                .orElseThrow(() -> new ObjectMappingException("World values should not be modified in the config!"));

        double locX = value.getNode("posX").getDouble();
        double locY = value.getNode("posY").getDouble();
        double locZ = value.getNode("posZ").getDouble();

        Vector3d position = new Vector3d(locX, locY, locZ);

        double rotX = value.getNode("rotX").getDouble();
        double rotY = value.getNode("rotY").getDouble();
        double rotZ = value.getNode("rotZ").getDouble();

        Vector3d rotation = new Vector3d(rotX, rotY, rotZ);

        return new Transform<>(world, position, rotation);
    }

    @Override
    public void serialize(TypeToken<?> type, Transform<World> transform, ConfigurationNode value) throws ObjectMappingException {
        World world = transform.getExtent();

        value.getNode("world").setValue(world.getName());

        Vector3d position = transform.getPosition();

        value.getNode("posX").setValue(position.getX());
        value.getNode("posY").setValue(position.getY());
        value.getNode("posZ").setValue(position.getZ());

        Vector3d rotation = transform.getRotation();

        value.getNode("rotX").setValue(rotation.getX());
        value.getNode("rotY").setValue(rotation.getY());
        value.getNode("rotZ").setValue(rotation.getZ());
    }

}
