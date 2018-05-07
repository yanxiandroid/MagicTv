package com.yht.iptv.model;

/**
 * Created by admin on 2017/12/14.
 */

public class HotelGeneralInfo {

    /**
     * approvalTime : 2017-08-23 09:59:15
     * approvalUserId : 1
     * attachment : 906
     * attachments : {"contenttype":"application/octet-stream","description":"null","filelength":43993607,"filename":"怡景湾大酒店宣传片MP4.mp4","filenameindisk":"http://10.0.10.103:8080/largeFileUpload/upload/attachment/2017/06/16/d9f04753-3ad5-45c4-a8ba-a63fce37f89e.mp4","id":906,"memberID":"111","needwatermark":1}
     * details : 11
     * fileUpload : {"fileName":"3.jpg","fileType":"jpg","id":890,"path":"http://10.0.10.104:8080/hotelServer/images/1.4976010107507717E12.jpg"}
     * id : 1
     * isPass : 1
     * name : 酒店简介
     * path : 890
     * remark :
     * uploadTime : 2017-08-23 09:59:09
     * uploadUserId : 1
     */

    private String approvalTime;
    private int approvalUserId;
    private String attachment;
    private AttachmentsBean attachments;
    private String details;
    private FileUploadBean fileUpload;
    private int id;
    private int isPass;
    private String name;
    private int path;
    private String remark;
    private String uploadTime;
    private int uploadUserId;

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public int getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(int approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public AttachmentsBean getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentsBean attachments) {
        this.attachments = attachments;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(int uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public static class AttachmentsBean {
        /**
         * contenttype : application/octet-stream
         * description : null
         * filelength : 43993607
         * filename : 怡景湾大酒店宣传片MP4.mp4
         * filenameindisk : http://10.0.10.103:8080/largeFileUpload/upload/attachment/2017/06/16/d9f04753-3ad5-45c4-a8ba-a63fce37f89e.mp4
         * id : 906
         * memberID : 111
         * needwatermark : 1
         */

        private String contenttype;
        private String description;
        private int filelength;
        private String filename;
        private String filenameindisk;
        private int id;
        private String memberID;
        private int needwatermark;

        public String getContenttype() {
            return contenttype;
        }

        public void setContenttype(String contenttype) {
            this.contenttype = contenttype;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getFilelength() {
            return filelength;
        }

        public void setFilelength(int filelength) {
            this.filelength = filelength;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFilenameindisk() {
            return filenameindisk;
        }

        public void setFilenameindisk(String filenameindisk) {
            this.filenameindisk = filenameindisk;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }

        public int getNeedwatermark() {
            return needwatermark;
        }

        public void setNeedwatermark(int needwatermark) {
            this.needwatermark = needwatermark;
        }
    }

    public static class FileUploadBean {
        /**
         * fileName : 3.jpg
         * fileType : jpg
         * id : 890
         * path : http://10.0.10.104:8080/hotelServer/images/1.4976010107507717E12.jpg
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
