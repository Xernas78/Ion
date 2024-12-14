package dev.xernas.packets.login;

import dev.xernas.IonParticle;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;
import dev.xernas.particle.Particle;

import java.util.UUID;

public class LoginStartPacket implements IPacket {

    private String username;

    public LoginStartPacket() {}

    public LoginStartPacket(String username) {
        this.username = username;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("username", particle.readString(16));
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeString(username, 16);
    }
}
