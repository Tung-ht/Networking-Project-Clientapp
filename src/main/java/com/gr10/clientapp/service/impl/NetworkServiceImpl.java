package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.entity.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Component
public class NetworkServiceImpl {

    private String HOST_ADDRESS;
    private int PORT;
    private int BLOCK_SIZE;
    private int FILE_MAX_SIZE;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private ObservableList<FileInfo> files;

    @Autowired
    public NetworkServiceImpl(@Value("${socket.host}") String HOST_ADDRESS,
                              @Value("${socket.port}") int PORT,
                              @Value("${socket.block-size}") int BLOCK_SIZE,
                              @Value("${file-max-size}") int FILE_MAX_SIZE) throws IOException {

        this.socket = new Socket(HOST_ADDRESS, PORT);
        in = new DataInputStream(this.socket.getInputStream());
        out = new DataOutputStream(this.socket.getOutputStream());

    }


    public ObservableList<FileInfo> getFiles() {
        FileInfo file1 = new FileInfo(3,"file1.exe", "30mb");
        FileInfo file2 = new FileInfo(3,"file2.exe", "30mb");
        FileInfo file3 = new FileInfo(3,"file3.exe", "30mb");
        this.files = FXCollections.observableArrayList(file1, file2, file3);
        System.out.println(HOST_ADDRESS + " " + PORT + " " + BLOCK_SIZE + " " + FILE_MAX_SIZE);
        return files;
    }

    public void deleteFile(FileInfo selectedFile) {
        files.removeAll(selectedFile);
    }
}
