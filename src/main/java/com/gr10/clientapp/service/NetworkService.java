package com.gr10.clientapp.service;

import com.gr10.clientapp.entity.FileInfo;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;

public interface NetworkService {
    void createFolder(String username) throws IOException;
    ObservableList<FileInfo> getFilesInfo();
    void uploadFile(File file) throws IOException;
    void downloadFile(FileInfo selectedFile);
    void deleteFile(FileInfo selectedFile) throws IOException;
    void logout() throws IOException;
}
