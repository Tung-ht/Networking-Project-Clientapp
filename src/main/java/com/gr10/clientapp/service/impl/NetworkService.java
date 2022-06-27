package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.entity.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;

@Component
public class NetworkService {

    @Value("${udp.port}")
    private int PORT;

    @Value("${udp.block-size}")
    private int BLOCK_SIZE;

    private DatagramSocket serverSocket;

    private ObservableList<FileInfo> files;

    @Autowired
    public NetworkService(@Value("${udp.port}") int PORT,
                          @Value("${udp.block-size}") int BLOCK_SIZE) {
        FileInfo file1 = new FileInfo(1,"file1.exe", "10mb");
        FileInfo file2 = new FileInfo(2,"file2.exe", "20mb");
        FileInfo file3 = new FileInfo(3,"file3.exe", "30mb");
        this.files = FXCollections.observableArrayList(file1, file2, file3);
        System.out.println(PORT + BLOCK_SIZE);
    }

    public ObservableList<FileInfo> getFiles() {
        return files;
    }

    public void deleteFile(FileInfo selectedFile) {
        files.removeAll(selectedFile);
    }
}
