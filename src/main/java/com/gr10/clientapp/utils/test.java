package com.gr10.clientapp.utils;

import java.net.*;
import java.io.*;

public class test {
    // initialize socket and input output streams
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    // constructor to put ip address and port
    public test(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            DataPackageSent testMsg = new DataPackageSent();
//            testMsg.addChar('2');
            short a = 9997;

            testMsg.addShort(a);
            testMsg.sendPacket(dos);
//            dos.write(addShort((short)9997), 0, 2);
            dis.close();
            dos.close();
            socket.close();
            System.out.println("closed!");
        } catch (IOException u) {
            System.out.println(u);
        }
    }

    public static byte[] addShort(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }

    char readOpcode(DataInputStream dis) throws IOException {
        return (char) (dis.readByte() & 0xFF);
    }

    int readUnsignedShort(DataInputStream dis) throws IOException {
        return dis.readShort() & 0xFFFF;
    }

    // sb.setLength(0) to clear StringBuilder
    // int bytesRead = input.readByte();
    // if (bytesRead == -1) -> it's end of stream

    StringBuilder readString(DataInputStream dis) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c = (char) (dis.readByte() & 0xFF);
        while (c != '\0') {
            sb.append(c);
            c = (char) (dis.readByte() & 0xFF);
        }
        return sb;
    }
//"0.tcp.ap.ngrok.io", 15056
    public static void main(String args[]) {
        test client = new test("localhost", 8888);
    }

}

