package dev.xernas.ion.packets;

import dev.xernas.ion.IonParticle;
import dev.xernas.particle.Particle;

import java.util.Locale;

public class ClientInformationPacket implements IPacket {

    private String locale;
    private byte viewDistance;
    private int chatMode;
    private boolean chatColors;
    private int skinParts;
    private int mainHand;
    private boolean enableTextFiltering;
    private boolean allowServerListings;

    public ClientInformationPacket() {}

    public ClientInformationPacket(Locale locale, byte viewDistance, int chatMode, boolean chatColors, int skinParts, boolean mainHand, boolean enableTextFiltering, boolean allowServerListings) {
        this.locale = locale.toLanguageTag();
        this.viewDistance = viewDistance;
        this.chatMode = chatMode;
        this.chatColors = chatColors;
        this.skinParts = skinParts;
        this.mainHand = mainHand ? 1 : 0;
        this.enableTextFiltering = enableTextFiltering;
        this.allowServerListings = allowServerListings;
    }

    @Override
    public void read(PacketData packetData, IonParticle particle) throws Particle.ReadException {
        packetData.put("locale", particle.readString(16));
        packetData.put("viewDistance", (byte) particle.readByte(true));
        packetData.put("chatMode", particle.readVarInt());
        packetData.put("chatColors", particle.readBoolean());
        packetData.put("skinParts", particle.readByte(false));
        packetData.put("mainHand", particle.readVarInt());
        packetData.put("enableTextFiltering", particle.readBoolean());
        packetData.put("allowServerListings", particle.readBoolean());
    }

    @Override
    public void write(IonParticle particle) throws Particle.WriteException {
        particle.writeString(locale);
        particle.writeByte(viewDistance);
        particle.writeVarInt(chatMode);
        particle.writeBoolean(chatColors);
        particle.writeByte(skinParts);
        particle.writeVarInt(mainHand);
        particle.writeBoolean(enableTextFiltering);
        particle.writeBoolean(allowServerListings);
    }

}
