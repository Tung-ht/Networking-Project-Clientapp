package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.FXApplication;
import com.gr10.clientapp.entity.FileInfo;
import com.gr10.clientapp.service.NetworkService;
import com.gr10.clientapp.utils.DataPackageUtils;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

@Component
public class NetworkServiceImpl implements NetworkService {

    private String HOST_ADDRESS;
    private int PORT;
    private int BLOCK_SIZE;
    private int FILE_MAX_SIZE;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObservableList<FileInfo> files;

    @Autowired
    public NetworkServiceImpl(@Value("${socket.host}") String HOST_ADDRESS,
                              @Value("${socket.port}") int PORT,
                              @Value("${socket.block-size}") int BLOCK_SIZE,
                              @Value("${file-max-size}") int FILE_MAX_SIZE) throws IOException {
        System.out.println(HOST_ADDRESS + " " + PORT + " " + BLOCK_SIZE + " " + FILE_MAX_SIZE);
        this.socket = new Socket(HOST_ADDRESS, PORT);
        dis = new DataInputStream(this.socket.getInputStream());
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void createFolder(String username) throws IOException {
        DataPackageUtils.writeOpcode('0', dos);
        DataPackageUtils.writeString(FXApplication.username, dos);
    }

    public ObservableList<FileInfo> getFiles(String username) throws IOException {

        // send msg to server
        DataPackageUtils.writeOpcode('1', dos);
        DataPackageUtils.writeString(FXApplication.username, dos);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // read msg from server
        DataPackageUtils.readOpcode(dis);

        int i = 1;
        StringBuilder sb = DataPackageUtils.readString(dis);
        while (sb != null) {
            String fileName = sb.toString();
            String fileSize = String.valueOf(DataPackageUtils.readShort(dis));
            FileInfo fileInfo = new FileInfo(i, fileName, fileSize);
            this.files.add(fileInfo);

            // read next file info
            sb = DataPackageUtils.readString(dis);
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
