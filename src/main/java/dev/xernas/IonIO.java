package dev.xernas;

import dev.xernas.enums.Bound;
import dev.xernas.particle.Particle;
import dev.xernas.particle.ParticleException;
import dev.xernas.particle.message.MessageIO;
import dev.xernas.packets.IPacket;
import dev.xernas.packets.PacketData;
import dev.xernas.packets.PacketRegistry;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class IonIO implements MessageIO<PacketData, IPacket> {

    private final ClientSession session;

    public IonIO(ClientSession session) {
        this.session = session;
    }

    @Override
    public PacketData read(Particle particle) throws Particle.ReadException {
        IonParticle ionParticle = new IonParticle(particle);
        int packetLength = ionParticle.readVarInt();
        System.out.println("Packet Length: " + packetLength);
        int packetId = ionParticle.readVarInt();
        System.out.println("Packet ID: " + packetId);

        try {
            IPacket packet = PacketRegistry.get(session.getState(), Bound.INBOUND, packetId);
            if (packet == null) {
                throw new Particle.ReadException("Unknown packet id : " + packetId);
            }
            PacketData packetData = new PacketData();
            packetData.put("packetId", packetId);
            packetData.put("packetLength", packetLength);

            try {
                packet.read(packetData, new IonParticle(particle, packetLength));
            } catch (ParticleException e) {
                throw new Particle.ReadException("Failed to read packet data", e);
            }
            return packetData;
        } catch (ParticleException e) {
            throw new Particle.ReadException(e.getMessage());
        }
    }

    @Override
    public void write(IPacket iPacket, Particle particle) throws Particle.WriteException {
        IonParticle ionParticle = new IonParticle(particle);
        int packetId = PacketRegistry.getId(session.getState(), Bound.OUTBOUND, iPacket.getClass());
        if (packetId == -1) {
            throw new Particle.WriteException("Unknown packet: " + iPacket.getClass().getSimpleName());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IonParticle tempParticle = new IonParticle(new Particle(new DataOutputStream(byteArrayOutputStream)));
        tempParticle.writeVarInt(packetId);
        iPacket.write(tempParticle);
        byte[] packetData = byteArrayOutputStream.toByteArray();
        ionParticle.writeVarInt(packetData.length);
        ionParticle.writeBytes(packetData);
    }
}
