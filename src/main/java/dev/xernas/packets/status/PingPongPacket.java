package dev.xernas.packets.status;

import dev.xernas.IonParticle;
import dev.xernas.particle.Particle;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;

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
