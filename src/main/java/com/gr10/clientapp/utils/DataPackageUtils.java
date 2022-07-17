package com.gr10.clientapp.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public final class DataPackageUtils {

    public static void writeOpcode(char c, DataOutputStream dos) throws IOException {
        int ascii = (int) c;
        dos.writeByte(ascii);
    }

    public static void writeShort(short sh, DataOutputStream dos) throws IOException {
        dos.write(sh);
    }

    public static void writeString(String s, DataOutputStream dos) throws IOException {
        dos.writeBytes(s);
    }

    public static char readOpcode(DataInputStream dis) throws IOException {
        return (char) (dis.readByte() & 0xFF);
    }

    public static short readShort(DataInputStream dis) throws IOException {
        return dis.readShort();
    }

    // sb.setLength(0) to clear StringBuilder
    // int bytesRead = input.readByte();
    // if (bytesRead == -1) -> it's end of stream
    public static StringBuilder readString(DataInputStream dis) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            char c = (char) (dis.readByte() & 0xFF);
            while (c != '\0') {
                sb.append(c);
                c = (char) (dis.readByte() & 0xFF);
            }
            return sb;
        } catch (EOFException e) {
            return null;
        }
    }

}
