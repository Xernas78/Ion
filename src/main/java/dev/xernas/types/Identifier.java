package dev.xernas.types;

public class Identifier {

    private final String namespace;
    private final String value;

    public Identifier(String namespace, String value) {
        this.namespace = namespace;
        this.value = value;
    }

    @Override
    public String toString() {
        return (namespace + ":" + value).toLowerCase();
    }

    public static Identifier fromString(String string) {
        String[] split = string.split(":");
        return new Identifier(split[0], split[1]);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getValue() {
        return value;
    }

    public int getLength() {
        return toString().getBytes().length;
    }
}
