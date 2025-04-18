package dev.xernas.ion.packets;

import dev.xernas.ion.IonParticle;
import dev.xernas.particle.Particle;

public interface IPacket {

    void read(PacketData packetData, IonParticle particle) throws Particle.ReadException;

    void write(IonParticle particle) throws Particle.WriteException;

}
