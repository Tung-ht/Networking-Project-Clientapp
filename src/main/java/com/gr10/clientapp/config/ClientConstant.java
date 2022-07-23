package com.gr10.clientapp.config;

public final class ClientConstant {

    public static final String HOST_ADDRESS = "0.tcp.ap.ngrok.io";
    public static final int PORT            = 16433;
    public static final int TIME_OUT        = 3000;

    // for sending file data
    public static final int MAX_DATA_BLOCK_SIZE     = 1024;                         // 1 file data block <= 1024 byte
    public static final int MAX_PACKAGE_SIZE        = 1+2+ MAX_DATA_BLOCK_SIZE;     // include opcode & a short (number of block)
    public static final int FILE_MAX_SIZE           = MAX_DATA_BLOCK_SIZE *1024*5;  // file size <= 5 mB
    public static final int MAX_BLOCK_NUMBER        = 1024*5;                       // = fileMaxSize/blockSize

    // opcode
    public static final char OPCODE_CREATE      = '0';
    public static final char OPCODE_LIST        = '1';
    public static final char OPCODE_UPLOAD      = '3';
    public static final char OPCODE_DOWNLOAD    = '4';
    public static final char OPCODE_DATA_BLOCK  = '5';
    public static final char OPCODE_CONFIRM     = '6';
    public static final char OPCODE_DELETE      = '7';
    public static final char OPCODE_ERROR       = '8';
    public static final char OPCODE_EXIT        = '9';

    private ClientConstant() { }

}