package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by admin on 2017/2/23.
 * 音乐详情
 */
public class MusicDetailBean implements Parcelable {

    /**
     * fileUpload : {"fileName":"李翊君 - 雨蝶.mp3","fileType":"mp3","id":709,"path":"http://10.0.10.104:8080/hotelServer/images//1.4876673053642515E12.mp3"}
     * id : 5
     * isPass : 0
     * musicType : {"id":2,"name":"流行音乐"}
     * musicTypeId : 2
     * name : 雨蝶
     * path : 709
     * uploadTime : 2017-02-21 17:51:11
     * uploadUserId : 1
     */

    private FileUploadBean fileUpload;
    private int id;
    private int isPass;
    private MusicTypeBean musicType;
    private int musicTypeId;
    private String name;
    private int path;
    private String uploadTime;
    private int uploadUserId;

    public static Creator<MusicDetailBean> getCreator() {
        return CREATOR;
    }

    // 属性是必须的要有的
    public static final Creator<MusicDetailBean> CREATOR = new Creator<MusicDetailBean>() {
        public MusicDetailBean createFromParcel(Parcel in) {
            MusicDetailBean data = new MusicDetailBean();
            data.fileUpload = (FileUploadBean) in.readValue(FileUploadBean.class.getClassLoader());
            data.id = in.readInt();
            data.isPass = in.readInt();
            data.musicType =(MusicTypeBean) in.readValue(FileUploadBean.class.getClassLoader());
            data.musicTypeId = in.readInt();
            data.name = in.readString();
            data.path = in.readInt();
            data.uploadTime = in.readString();
            data.uploadUserId = in.readInt();
            return data;
        }

        @Override
        public MusicDetailBean[] newArray(int size) {
            return new MusicDetailBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fileUpload);
        dest.writeInt(id);
        dest.writeInt(isPass);
        dest.writeValue(musicType);
        dest.writeInt(musicTypeId);
        dest.writeString(name);
        dest.writeInt(path);
        dest.writeString(uploadTime);
        dest.writeInt(uploadUserId);
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

    public MusicTypeBean getMusicType() {
        return musicType;
    }

    public void setMusicType(MusicTypeBean musicType) {
        this.musicType = musicType;
    }

    public int getMusicTypeId() {
        return musicTypeId;
    }

    public void setMusicTypeId(int musicTypeId) {
        this.musicTypeId = musicTypeId;
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

    public static class FileUploadBean implements Serializable{
        /**
         * fileName : 李翊君 - 雨蝶.mp3
         * fileType : mp3
         * id : 709
         * path : http://10.0.10.104:8080/hotelServer/images//1.4876673053642515E12.mp3
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

    public static class MusicTypeBean implements Serializable {
        /**
         * id : 2
         * name : 流行音乐
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

    @Override
    public String toString() {
        return "MusicDetailBean{" +
                "fileUpload=" + fileUpload +
                ", id=" + id +
                ", isPass=" + isPass +
                ", musicType=" + musicType +
                ", musicTypeId=" + musicTypeId +
                ", name='" + name + '\'' +
                ", path=" + path +
                ", uploadTime='" + uploadTime + '\'' +
                ", uploadUserId=" + uploadUserId +
                '}';
    }
}
