package com.gr10.clientapp.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    private Integer fileNo;
    private String fileName;
    private String fileSize;

    private int piecesOfFile;
    private int lastByteLength;

    public FileInfo(Integer fileNo, String fileName, String fileSize) {
        this.fileNo = fileNo;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}
