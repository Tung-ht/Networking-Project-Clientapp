package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.FXApplication;
import com.gr10.clientapp.entity.FileInfo;
import com.gr10.clientapp.service.NetworkService;
import com.gr10.clientapp.utils.DataPackageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class NetworkServiceImpl implements NetworkService {

    private final String HOST_ADDRESS = "172.16.184.133";
    private final int PORT = 8888;
    private final int BLOCK_SIZE = 1027;
    private final int FILE_MAX_SIZE = 10485760;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObservableList<FileInfo> files = FXCollections.observableArrayList();;

    public NetworkServiceImpl() throws IOException {
        this.socket = new Socket(HOST_ADDRESS, PORT);
        dis = new DataInputStream(this.socket.getInputStream());
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void createFolder(String username) throws IOException {
//        DataPackageUtils.writeOpcode('0', dos);
        DataPackageUtils.writeString("0" + FXApplication.username, dos);
        DataPackageUtils.readString(dis);
    }

    public ObservableList<FileInfo> getFiles(String username) throws IOException {

        // send msg to server
//        DataPackageUtils.writeOpcode('1', dos);
        DataPackageUtils.writeString("1" + FXApplication.username, dos);

        // read msg from server
        DataPackageUtils.readOpcode(dis);

        int folder_size = DataPackageUtils.readUnsignedShort(dis);

        for (int i = 0; i < folder_size; i++) {
            String fileName = DataPackageUtils.readString(dis).toString();
            String fileSize = String.valueOf(DataPackageUtils.readUnsignedShort(dis));
            FileInfo fileInfo = new FileInfo(i+1, fileName, fileSize + " kB");
            this.files.add(fileInfo);
        }
        return this.files;
    }

    @Override
    public ObservableList<FileInfo> getFilesInfo() {
        return null;
    }

    @Override
    public void uploadFile(File file) {

    }

    @Override
    public void downloadFile(String fileName) {

    }

    @Override
    public void deleteFile(FileInfo selectedFile) {
        files.removeAll(selectedFile);
    }

    @Override
    public void logout() throws IOException {
        dis.close();
        dos.close();
        socket.close();
    }
}
