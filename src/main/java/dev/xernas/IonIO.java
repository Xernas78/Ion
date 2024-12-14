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

        try {
            IonParticle tempParticle = new IonParticle(particle, packetLength);
            int packetId = tempParticle.readVarInt();

            IPacket packet = PacketRegistry.get(session.getState(), Bound.SERVERBOUND, packetId);
            if (packet == null) {
                throw new Particle.ReadException("Unknown packet id : " + packetId);
            }
            PacketData packetData = new PacketData();
            packetData.put("packetId", packetId);
            packetData.put("packetLength", packetLength);

            packet.read(packetData, tempParticle);
            tempParticle.close();

            return packetData;
        } catch (ParticleException e) {
            throw new Particle.ReadException("Failed to read packet data : " + e.getMessage());
        }
    }

    @Override
    public void write(IPacket iPacket, Particle particle) throws Particle.WriteException {
        IonParticle ionParticle = new IonParticle(particle);
        int packetId = PacketRegistry.getId(session.getState(), Bound.CLIENTBOUND, iPacket.getClass());
        if (packetId == -1) {
            throw new Particle.WriteException("Unknown packet: " + iPacket.getClass().getSimpleName());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IonParticle tempParticle = new IonParticle(new DataOutputStream(byteArrayOutputStream));
        tempParticle.writeVarInt(packetId);
        iPacket.write(tempParticle);
        byte[] packetData = byteArrayOutputStream.toByteArray();
        ionParticle.writeVarInt(packetData.length);
        ionParticle.writeBytes(packetData);
        ionParticle.flush();
        try {
            tempParticle.close();
        } catch (ParticleException e) {
            throw new Particle.WriteException(e.getMessage());
        }
    }

    public ClientSession getSession() {
        return session;
    }
}
