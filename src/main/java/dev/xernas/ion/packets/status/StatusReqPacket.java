package dev.xernas.ion.packets.status;

import dev.xernas.ion.IonParticle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;
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
