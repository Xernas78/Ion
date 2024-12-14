package dev.xernas.packets.status;

import dev.xernas.IonParticle;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;
import dev.xernas.particle.Particle;

public class StatusReqPacket implements IPacket {

    public StatusReqPacket() {}

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {

    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {

    }
}
