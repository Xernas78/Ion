package dev.xernas.ion.packets.status;

import dev.xernas.ion.IonParticle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;
import dev.xernas.particle.Particle;

public class StatusResPacket implements IPacket {

    private String json;

    public StatusResPacket() {}

    public StatusResPacket(String json) {
        this.json = json;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("json", particle.readString());
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeString(json);
    }
}
