package com.yht.iptv.model;

/**
 * 图片广告数据
 * Created by Q on 2018/1/6.
 */

public class PictureAdInfo {

    /**
     * category : {"id":5,"name":"地产"}
     * fileUpload : {"fileName":"杭州映月台","fileType":"jpg","id":1627,"path":"http://10.0.10.104:8080/hotelServer/images/ad/image/d89cfb9526ba45740aa025c15ca9e8b1311be8f416442a-glDuJ7.jpg"}
     * id : 2
     * name : 杭州映月台
     * type : 2
     */

    private CategoryBean category;
    private FileUploadBean fileUpload;
    private int id;
    private String name;
    private int type;


    public FileUploadBean getFileUpload() {
        return fileUpload;
    }

    public CategoryBean getCategory() {
        return category;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class CategoryBean {
        /**
         * id : 5
         * name : 地产
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
         * fileName : 杭州映月台
         * fileType : jpg
         * id : 1627
         * path : http://10.0.10.104:8080/hotelServer/images/ad/image/d89cfb9526ba45740aa025c15ca9e8b1311be8f416442a-glDuJ7.jpg
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
