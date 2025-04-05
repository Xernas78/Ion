package dev.xernas.ion;

import dev.xernas.ion.enums.Bound;
import dev.xernas.particle.Particle;
import dev.xernas.particle.ParticleException;
import dev.xernas.particle.message.MessageIO;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;
import dev.xernas.ion.packets.PacketRegistry;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IonIO implements MessageIO<PacketData, IPacket> {

    private final ClientSession session;
    private final boolean ignoreUnknownPackets;

    public IonIO(ClientSession session) {
        this(session, false);
    }

    public IonIO(ClientSession session, boolean ignoreUnknownPackets) {
        this.session = session;
        this.ignoreUnknownPackets = ignoreUnknownPackets;
    }

    @Override
    public PacketData read(Particle particle) throws Particle.ReadException {
        IonParticle ionParticle = new IonParticle(particle);
        int packetLength = ionParticle.readVarInt();

        try {
            IonParticle tempParticle = new IonParticle(particle, packetLength);
            tempParticle.in().mark(packetLength);
            int packetId = tempParticle.readVarInt();

            IPacket packet = PacketRegistry.get(session.getState(), Bound.SERVERBOUND, packetId);
            if (packet == null) {
                if (!ignoreUnknownPackets) throw new Particle.ReadException("Unknown packet id : " + packetId);
                try {
                    tempParticle.in().reset();
                } catch (IOException e) {
                    throw new ParticleException(e.getMessage());
                }
                byte[] packetBytes = tempParticle.readBytes(packetLength);
                PacketData packetData = new PacketData(false);
                packetData.put("packetId", packetId);
                packetData.put("packetLength", packetLength);
                packetData.put("packetBytes", packetBytes);
                tempParticle.close();
                return packetData;
            }
            PacketData packetData = new PacketData(true);
            packetData.put("packetId", packetId);
            packetData.put("packetLength", packetLength);

            packet.read(packetData, tempParticle);

            // Go to the beginning of the stream and read the bytes of the packet
            try {
                tempParticle.in().reset();
            } catch (IOException e) {
                throw new ParticleException(e.getMessage());
            }
            byte[] packetBytes = tempParticle.readBytes(packetLength);
            packetData.put("packetBytes", packetBytes);

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
