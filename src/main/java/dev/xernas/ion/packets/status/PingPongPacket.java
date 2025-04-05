package dev.xernas.ion.packets.status;

import dev.xernas.ion.IonParticle;
import dev.xernas.particle.Particle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;

public class PingPongPacket implements IPacket {

    private long timestamp;

    public PingPongPacket() {

    }

    public PingPongPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("timestamp", particle.readLong());
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeLong(timestamp);
    }
}
