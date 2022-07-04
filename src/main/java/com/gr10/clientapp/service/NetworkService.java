package com.gr10.clientapp.service;

import com.gr10.clientapp.entity.FileInfo;
import javafx.collections.ObservableList;

import java.io.File;

public interface NetworkService {
    boolean createFolder(String username);
    ObservableList<FileInfo> getFilesInfo(String username);
    void uploadFile(String username, File file);
    void downloadFile(String username, String fileName);
    void deleteFile(FileInfo selectedFile);
}
