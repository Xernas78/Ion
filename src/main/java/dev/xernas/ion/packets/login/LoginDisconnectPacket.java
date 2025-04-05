package dev.xernas.ion.packets.login;

import dev.xernas.ion.IonParticle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;
import dev.xernas.particle.Particle;
import dev.xernas.ion.types.ChatComponent;

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
