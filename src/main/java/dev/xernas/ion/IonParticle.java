package dev.xernas.ion;

import dev.xernas.particle.Particle;
import dev.xernas.particle.ParticleException;
import dev.xernas.ion.types.Identifier;
import dev.xernas.ion.types.Property;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IonParticle extends Particle {

    private final int SEGMENT_BITS = 0x7F;
    private final int CONTINUE_BIT = 0x80;

    public IonParticle(DataInputStream in) {
        super(in);
    }

    public IonParticle(DataOutputStream out) {
        super(out);
    }

    public IonParticle(Particle particle) {
        super(particle.in(), particle.out());
    }

    public IonParticle(Particle particle, int length) throws ParticleException {
        super(particle.in(), length);
    }

    public int readVarInt() throws ReadException {
        try {
            int value = 0;
            int position = 0;
            byte currentByte;

            while (true) {
                if (in().available() == 0) {
                    throw new EOFException("No data available to read");
                }
                currentByte = in().readByte();
                value |= (currentByte & SEGMENT_BITS) << position;
                if ((currentByte & CONTINUE_BIT) == 0) break;

                position += 7;

                if (position >= 32) throw new RuntimeException("VarInt is too big");
            }

            return value;
        } catch (IOException e) {
            throw new ReadException("Couldn't read VarInt", e);
        }
    }

    public void writeVarInt(int value) throws WriteException {
        try {
            while (true) {
                if ((value & ~SEGMENT_BITS) == 0) {
                    out().writeByte(value);
                    return;
                }

                out().writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

                // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
                value >>>= 7;
            }
        } catch (IOException e) {
            throw new WriteException("Couldn't write VarInt", e);
        }
    }

    public void writeString(String s, int i) throws WriteException {
        if (s.length() > i) {
            int j = s.length();

            throw new WriteException("String too big (was " + j + " characters, max " + i + ")");
        } else {
            byte[] abyte = s.getBytes(StandardCharsets.UTF_8);
            int k = i * 3;

            if (abyte.length > k) {
                throw new WriteException("String too big (was " + abyte.length + " bytes encoded, max " + k + ")");
            } else {
                writeVarInt(abyte.length);
                writeBytes(abyte);
            }
        }
    }

    public String readString(int limit) throws ReadException {
        int size = readVarInt();
        if (size > limit * 4) {
            throw new ReadException("String too long (" + size + " > " + limit + " * 4)");
        }
        byte[] stringBytes = readBytes(size);
        String string = new String(stringBytes);
        if (string.length() > limit) {
            throw new ReadException("String too long (" + string.length() + " > " + limit + ")");
        }
        return string;
    }

    @Override
    public void writeString(String s) throws WriteException {
        writeString(s, 32767);
    }

    @Override
    public String readString() throws ReadException {
        return readString(32767);
    }

    public UUID readUUID() throws ReadException {
        return new UUID(readLong(), readLong());
    }

    public void writeUUID(UUID uuid) throws WriteException {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public Identifier readIdentifier() throws ReadException {
        return Identifier.fromString(readString());
    }

    public void writeIdentifier(Identifier identifier) throws WriteException {
        writeString(identifier.toString());
    }

    public void writeProperty(Property property) throws WriteException {
        writeString(property.getName(), 32767);
        writeString(property.getValue(), 32767);
        writeBoolean(property.getSignature() != null && !property.getSignature().isEmpty());
        if (property.getSignature() != null) {
            writeString(property.getSignature(), 32767);
        }
    }

    public Property readProperty() throws ReadException {
        String name = readString();
        String value = readString();
        String signature = null;
        if (readBoolean()) signature = readString();
        if (signature == null) return new Property(name, value);
        else return new Property(name, value, signature);
    }

    public void writeProperties(List<Property> properties) throws WriteException {
        writeVarInt(properties.size());
        for (Property property : properties) writeProperty(property);
    }

    public List<Property> readProperties() throws ReadException {
        int size = readVarInt();
        List<Property> properties = new ArrayList<>(size);
        for (int i = 0; i < size; i++) properties.add(readProperty());
        return properties;
    }
}
