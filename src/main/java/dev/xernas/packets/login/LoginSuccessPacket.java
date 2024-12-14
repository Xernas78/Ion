package dev.xernas.packets.login;

import dev.xernas.IonParticle;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;
import dev.xernas.particle.Particle;

import java.util.UUID;

public class LoginSuccessPacket implements IPacket {

    private UUID uuid;
    private String username;

    public LoginSuccessPacket() {}

    public LoginSuccessPacket(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("uuid", particle.readUUID());
        packetData.put("username", particle.readString(16));
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeUUID(uuid);
        particle.writeString(username, 16);
    }

}
