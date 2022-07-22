package com.gr10.clientapp.utils;

import com.gr10.clientapp.config.ClientConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;

// build message to send to the server
@Slf4j
public class DataPackageSent {

    public byte[] data;
    protected int current_packet_size;
    protected int package_no;

    public DataPackageSent() {
        current_packet_size = 0;
        package_no = 0;
        data = new byte[ClientConstant.MAX_PACKAGE_SIZE];
    }

    public void clear() {
        current_packet_size = 0;
        for (int i = 0; i < ClientConstant.MAX_PACKAGE_SIZE; i++) data[i] = 0;
    }

    public int getSize() {
        return current_packet_size;
    }

    public void setSize(int size) throws RuntimeException {
        if (size < ClientConstant.MAX_PACKAGE_SIZE) {
            current_packet_size = size;
        } else {
            throw new RuntimeException("Packet size exceeded");
        }
    }

    public boolean addChar(char c) {
        if (current_packet_size + 1 > ClientConstant.MAX_PACKAGE_SIZE) {
            return false;
        }
        data[current_packet_size] = (byte) c;
        current_packet_size++;
        return true;
    }

    public boolean addByte(byte b) {
        if (current_packet_size + 1 > ClientConstant.MAX_PACKAGE_SIZE) {
            return false;
        }
        data[current_packet_size] = b;
        current_packet_size++;
        return true;
    }

    public boolean addShort(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);

        if (current_packet_size + 2 > ClientConstant.MAX_PACKAGE_SIZE) {
            return false;
        }
        data[current_packet_size] = b[0];
        current_packet_size ++;
        data[current_packet_size] = b[1];
        current_packet_size ++;
        return true;
    }

    public boolean addString(String s) {
        byte[] b;
        b = s.getBytes();
        for (int i = 0; i < s.length(); i++) {
            if (!addByte(b[i])) {
                return false;
            }
        }
        addByte((byte) '\0');
        return true;
    }

    public boolean sendPacket(DataOutputStream out) {
        try {
            out.write(data, 0, getSize());
            out.flush();
            return true;
        } catch (Exception e) {
            log.debug("Exception in sendPacket()");
            return false;
        }
    }

    public void dumpData() {
        log.debug("--------------DATA DUMP---------------------");
        log.debug("Packet Size: " + current_packet_size);
        log.debug(new String(data));
        log.debug("--------------------------------------------");
    }
}
