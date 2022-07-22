package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.FXApplication;
import com.gr10.clientapp.config.ClientConstant;
import com.gr10.clientapp.entity.FileInfo;
import com.gr10.clientapp.service.NetworkService;
import com.gr10.clientapp.utils.AlertUtils;
import com.gr10.clientapp.utils.DataPackageReceived;
import com.gr10.clientapp.utils.DataPackageSent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

@Slf4j
public class NetworkServiceImpl implements NetworkService {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObservableList<FileInfo> files = FXCollections.observableArrayList();

    public NetworkServiceImpl() throws IOException {
        this.socket = new Socket(ClientConstant.HOST_ADDRESS, ClientConstant.PORT);
        dis = new DataInputStream(this.socket.getInputStream());
        dos = new DataOutputStream(this.socket.getOutputStream());

        // set timeout 3s to wait message from server, if after 3s can not read anything from dis (server), throw exception
        this.socket.setSoTimeout(ClientConstant.TIME_OUT);
    }

    @Override
    public void createFolder(String username) throws IOException {
        // build message to request create new folder, opcode: '0'
        DataPackageSent createFolderMsg = new DataPackageSent();
        createFolderMsg.addChar('0');
        createFolderMsg.addString(FXApplication.username);
        createFolderMsg.sendPacket(dos);

        // receive message from server
        log.debug("Server response: " + DataPackageReceived.readString(dis));
    }

    public ObservableList<FileInfo> getFiles(String username) throws IOException {
        // build message to request get list files info, opcode '1'
        DataPackageSent getFilesMsg = new DataPackageSent();
        getFilesMsg.addChar('1');
        getFilesMsg.addString(FXApplication.username);
        getFilesMsg.sendPacket(dos);

        // read msg from server
        log.debug("Server response: opcode: " + DataPackageReceived.readOpcode(dis));
        int folder_size = DataPackageReceived.readUnsignedShort(dis);
        log.debug("Server response: folder size: " + folder_size);

        for (int i = 0; i < folder_size; i++) {
            String fileName = DataPackageReceived.readString(dis).toString();
            String fileSize = String.valueOf(DataPackageReceived.readUnsignedShort(dis));
            log.debug("Server response: fileName: " + fileName + " | fileSize: " + fileSize + " kB");
            FileInfo fileInfo = new FileInfo(i + 1, fileName, fileSize + " kB");
            this.files.add(fileInfo);
        }
        return this.files;
    }

    @Override
    public ObservableList<FileInfo> getFilesInfo() {
        return null;
    }

    @Override
    public void uploadFile(File file) throws IOException {
        String fileName = file.getAbsoluteFile().getName();
        if (files.stream().anyMatch(fileInfo -> fileInfo.getFileName().equals(fileName))) {
            log.debug("File " + fileName + " already exist!");
            AlertUtils.showError("Error", "Duplicate file name", "File " + fileName + " already exists!");
            return;
        }
        long fileSize = file.length();
//            3 them are the same
//            System.out.println(file.getAbsolutePath() + "||||" + file.getPath() + "|||" + file.toPath());
        log.debug("Upload file | " + fileName + " | " + fileSize + " Byte");
        byte[] fileInBytes = Files.readAllBytes(file.toPath());
        int numberBlock = 0;
        if (fileSize % ClientConstant.MAX_DATA_BLOCK_SIZE == 0) {
            numberBlock = (int) (fileSize / ClientConstant.MAX_DATA_BLOCK_SIZE);
        } else {
            numberBlock = (int) (fileSize / ClientConstant.MAX_DATA_BLOCK_SIZE + 1);
        }
        log.debug("File number of blocks: " + numberBlock);
        // build message to request upload, opcode: '3'
        DataPackageSent uploadMsg = new DataPackageSent();
        uploadMsg.addChar('3');
        uploadMsg.addString(fileName);
        uploadMsg.addShort((short) numberBlock);
        uploadMsg.sendPacket(dos);

        // read msg from server
        log.debug("Server response: " + DataPackageReceived.readOpcode(dis));
        log.debug("Begin to send file!");
        int curFilePointer = 0;
        for (short blockNo = 0; blockNo < numberBlock; blockNo++) {
            // build message to send data block, opcode '5'
            DataPackageSent fileBlock = new DataPackageSent();
            fileBlock.addChar('5');
            fileBlock.addShort(blockNo);
            for (int i = 0; i < ClientConstant.MAX_DATA_BLOCK_SIZE; i++) {
                try {
                    fileBlock.addByte(fileInBytes[curFilePointer+i]);
                } catch (Exception e) {
                    log.debug(e.getMessage());
                    break;
                }
            }
            curFilePointer += ClientConstant.MAX_DATA_BLOCK_SIZE;

            fileBlock.sendPacket(dos);
            log.debug("Client sent data block no: " + blockNo);
            try {
                // wait ACK from server
                log.debug("Server response: " + DataPackageReceived.readOpcode(dis));
            } catch (Exception e) {
                // send error msg to server: opcode = '8'
                DataPackageSent errorMsg = new DataPackageSent();
                errorMsg.addChar('8');
                errorMsg.sendPacket(dos);
                AlertUtils.showError("Error", "Error when uploading file!", e.getMessage());
                break;
            }
        }
        log.debug("Upload file done!");
        AlertUtils.showConfirmation("Inform", "Upload file", "Upload file done!");
    }

    @Override
    public void downloadFile(FileInfo selectedFile) {
        String home = System.getProperty("user.home");
        System.out.println(home + "\\Downloads\\");
        String fileName = selectedFile.getFileName();
        try {
            File file = new File(home + "\\Downloads\\" + fileName);
            log.debug("Download file | " + fileName);
            if (file.createNewFile()) {
                log.debug("File " + fileName + " created!");
                BufferedOutputStream fileWriteBuffer = new BufferedOutputStream(new FileOutputStream(file));

                // build msg to request download, opcode: '4'
                DataPackageSent downloadMsg = new DataPackageSent();
                downloadMsg.addChar('4');
                downloadMsg.addString(fileName);
                downloadMsg.sendPacket(dos);
                log.debug("Begin download file! | " + fileName);

                // read msg from sv
                log.debug("Server response: opcode: " + DataPackageReceived.readOpcode(dis));
                int numberBlock = DataPackageReceived.readUnsignedShort(dis);
                log.debug("Server response: number of block: " + numberBlock);

                // send ACK msg
                DataPackageSent ackMsg = new DataPackageSent();
                ackMsg.addChar('6');
                ackMsg.sendPacket(dos);
                for (short blockNo = 0; blockNo < numberBlock; blockNo++) {
                    log.debug("Server response: opcode: " + DataPackageReceived.readOpcode(dis) + " | " + "block_no: " + DataPackageReceived.readUnsignedShort(dis));
                    // write 4096 byte to file
                    try {
                        for (int i = 0; i < ClientConstant.MAX_DATA_BLOCK_SIZE; i++) {
                            fileWriteBuffer.write(dis.read());
                        }
                    } catch (Exception ex) {
                        // it will be timeout on the last data block since dis.read() can not read enough 4096 byte
                        log.debug(ex.getMessage());
                    }
                    // send ACK, opcode: '6'
                    ackMsg.clear();
                    ackMsg.addChar('6');
                    ackMsg.addShort(blockNo);
                    ackMsg.sendPacket(dos);
                }
                log.debug("Download file done! | " + fileName);
                AlertUtils.showConfirmation("Inform", "Download file", "File " + fileName + " is downloaded!");
                fileWriteBuffer.close();
            } else {
                log.debug("File " + fileName + " already exists!");
                AlertUtils.showError("Error", "Error when downloading file!", "File " + fileName + " already exists!");
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Error when downloading file!", e.getMessage());
        }
    }

    @Override
    public void deleteFile(FileInfo selectedFile) throws IOException {
        files.removeAll(selectedFile);
        log.debug("Client request  | Delete file: " + selectedFile.getFileName());
        // build msg to request delete a file, opcode: '7'
        DataPackageSent dataPackageSent = new DataPackageSent();
        dataPackageSent.addChar('7');
        dataPackageSent.addString(selectedFile.getFileName());
        dataPackageSent.sendPacket(dos);
        // read msg from server
        log.debug("Server response: " + DataPackageReceived.readString(dis));
    }

    @Override
    public void logout() throws IOException {
        log.debug("Client request  | User request exit");
        // build msg to request exit, '9'
        DataPackageSent dataPackageSent = new DataPackageSent();
        dataPackageSent.addChar('9');
        dataPackageSent.sendPacket(dos);
        dis.close();
        dos.close();
        socket.close();
    }
}
