package dev.xernas.ion.packets;

import dev.xernas.ion.IonParticle;
import dev.xernas.particle.Particle;

public class EverythingPacket implements IPacket {

    private byte[] packetData;

    public EverythingPacket(byte[] packetData) {
        this.packetData = packetData;
    }

    @Override
    public void read(PacketData packetData, IonParticle ionParticle) throws Particle.ReadException {

    }

    @Override
    public void write(IonParticle ionParticle) throws Particle.WriteException {
        ionParticle.writeBytes(packetData);
    }
}
