package com.yht.iptv.model;

/**
 * Created by admin on 2018/1/5.
 */

public class AdvertVideoInfo {

    /**
     * category : {"id":2,"name":"食品"}
     * fileUpload : {"fileName":"【张天爱】哈根达斯冰淇凌广告30s版本 - 1.1(Av4792740,P1)[$1801051536$]","fileType":"mp4","id":1624,"path":"C:/apache-tomcat-8.0.0-RC1/webapps/hotelServer/images/ad/video/【张天爱】哈根达斯冰淇凌广告30s版本 - 1.1(Av4792740,P1)[$1801051536$]"}
     * id : 2
     * length : 30
     * name : 【张天爱】哈根达斯冰淇凌广告30s版本 - 1.1(Av4792740,P1)
     */

    private CategoryBean category;
    private FileUploadBean fileUpload;
    private int id;
    private int length;
    private String name;

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class CategoryBean {
        /**
         * id : 2
         * name : 食品
         */

        private int id;
        private String name;

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
    }

    public static class FileUploadBean {
        /**
         * fileName : 【张天爱】哈根达斯冰淇凌广告30s版本 - 1.1(Av4792740,P1)[$1801051536$]
         * fileType : mp4
         * id : 1624
         * path : C:/apache-tomcat-8.0.0-RC1/webapps/hotelServer/images/ad/video/【张天爱】哈根达斯冰淇凌广告30s版本 - 1.1(Av4792740,P1)[$1801051536$]
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
