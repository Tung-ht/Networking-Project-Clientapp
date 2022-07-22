package com.gr10.clientapp.utils;

import java.io.DataInputStream;
import java.io.IOException;

// read directly server message from input buffer
public final class DataPackageReceived {

    public static boolean isError(DataInputStream dis) throws IOException {
        return (char) (dis.readByte() & 0xFF) == '8';
    }

    public static char readOpcode(DataInputStream dis) throws IOException {
        return (char) (dis.readByte() & 0xFF);
    }

    public static int readUnsignedShort(DataInputStream dis) throws IOException {
        return dis.readShort() & 0xFFFF;
    }

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
