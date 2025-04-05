package dev.xernas.ion.packets.handshake;

import dev.xernas.ion.IonParticle;
import dev.xernas.particle.Particle;
import dev.xernas.ion.packets.IPacket;
import dev.xernas.ion.packets.PacketData;

public class HandshakePacket implements IPacket {

    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private int nextState;

    public HandshakePacket() {
    }

    public HandshakePacket(int protocolVersion, String serverAddress, int serverPort, int nextState) {
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nextState = nextState;
    }


    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("protocolVersion", particle.readVarInt());
        packetData.put("serverAddress", particle.readString());
        packetData.put("serverPort", particle.readShort(false));
        packetData.put("nextState", particle.readVarInt());
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeVarInt(protocolVersion);
        particle.writeString(serverAddress);
        particle.writeShort((short) serverPort);
        particle.writeVarInt(nextState);
    }
}
