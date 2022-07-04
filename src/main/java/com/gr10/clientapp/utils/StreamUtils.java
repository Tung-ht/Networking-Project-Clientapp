package com.gr10.clientapp.utils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class StreamUtils {

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8888;

        try (Socket socket = new Socket(hostname, port)) {
//            DataInputStream in = new DataInputStream(socket.getInputStream());
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//            System.out.println(in.readUTF());
//
//            out.writeUTF("hello server");
//            out.flush();
            String str1 = "hello ";
            String str2 = "server\n";
            socket.getOutputStream().write(str1.getBytes());
            socket.getOutputStream().write(str2.getBytes());
            socket.close();
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
