package com.gr10.clientapp.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class DataPackageUtils {

    public static void writeOpcode(char c, DataOutputStream dos) throws IOException {
        int ascii = (int) c;
        dos.writeByte(ascii);
    }

    public static void writeUnsignedShort(short sh, DataOutputStream dos) throws IOException {
        dos.writeShort(sh);
    }

    public static void writeString(String s, DataOutputStream dos) throws IOException {
        dos.write(s.getBytes(StandardCharsets.UTF_8));
    }

    public static char readOpcode(DataInputStream dis) throws IOException {
        return (char) (dis.readByte() & 0xFF);
    }

    public static int readUnsignedShort(DataInputStream dis) throws IOException {
        return dis.readShort() & 0xFFFF;
    }

    // sb.setLength(0) to clear StringBuilder
    // int bytesRead = input.readByte();
    // if (bytesRead == -1) -> it's end of stream
    public static StringBuilder readString(DataInputStream dis) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c = (char) (dis.readByte() & 0xFF);
        while (c != '\0') {
            sb.append(c);
            c = (char) (dis.readByte() & 0xFF);
        }
        return sb;
    }
}
