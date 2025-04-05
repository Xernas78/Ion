package dev.xernas.ion.packets.login;

import dev.xernas.ion.IonParticle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;
import dev.xernas.particle.Particle;

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
