package com.yht.iptv.model;

/**
 * Created by admin on 2016/8/9.
 */
public class AppMangerInfo {
    /**
     * fileUpload : {"fileName":"phone_IPTV-1_v1.0_release.apk","fileType":"apk","id":650,"path":"http://10.0.10.104:8080/hotelServer/images/1.4847091874360488E12.apk"}
     * id : 72
     * name : TV手机助手V.0.6.9
     * path : 650
     * type : 0
     * uploadTime : 2017-01-18 11:13:00
     * version : V.0.6.9
     */

    private FileUploadBean fileUpload;
    private int id;
    private String name;
    private int path;
    private int type;
    private String uploadTime;
    private String version;

    public FileUploadBean getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUploadBean fileUpload) {
        this.fileUpload = fileUpload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class FileUploadBean {
        /**
         * fileName : phone_IPTV-1_v1.0_release.apk
         * fileType : apk
         * id : 650
         * path : http://10.0.10.104:8080/hotelServer/images/1.4847091874360488E12.apk
         */

        private String fileName;
        private String fileType;
        private int id;
        private String path;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
