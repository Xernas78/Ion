package dev.xernas.packets.login;

import dev.xernas.IonParticle;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;
import dev.xernas.particle.Particle;
import dev.xernas.types.ChatComponent;

public class LoginDisconnectPacket implements IPacket {

    private ChatComponent reason;

    public LoginDisconnectPacket() {}

    public LoginDisconnectPacket(ChatComponent reason) {
        this.reason = reason;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("reason", particle.readString());
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeString(reason.toString());
    }

}
