package com.yht.iptv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by admin on 2016/8/8.
 */

public class MainPageInfo implements Parcelable, Serializable {

    private String id;
    private String enName;
    private String speech;
    private String zhName;
    private FileUpload fileUpload;
    private Logo logo;
    private String customerName;
    private String customerId;
    private String startingVideo;
    private PaymentSetting paymentSetting;
    private AdSetting adSetting;
    private long systemTime;


    public static Parcelable.Creator<MainPageInfo> getCreator() {
        return CREATOR;
    }

    // 属性是必须的要有的
    public static final Parcelable.Creator<MainPageInfo> CREATOR = new Parcelable.Creator<MainPageInfo>() {
        public MainPageInfo createFromParcel(Parcel in) {
            MainPageInfo data = new MainPageInfo();
            data.enName = in.readString();
            data.speech = in.readString();
            data.zhName = in.readString();
            data.fileUpload = (FileUpload) in.readValue(FileUpload.class.getClassLoader());
            data.logo = (Logo) in.readValue(Logo.class.getClassLoader());
            data.customerName = in.readString();
            data.customerId = in.readString();
            data.id = in.readString();
            data.startingVideo = in.readString();
            data.paymentSetting = (PaymentSetting) in.readValue(PaymentSetting.class.getClassLoader());
            data.adSetting = (AdSetting) in.readValue(AdSetting.class.getClassLoader());
            data.adSetting.mediaDetailImageFile = (MediaDetailImageFile) in.readValue(MediaDetailImageFile.class.getClassLoader());
            data.adSetting.mediaPlayImageFile = (MediaPlayImageFile) in.readValue(MediaPlayImageFile.class.getClassLoader());
            data.adSetting.musicPlayImageFile = (MusicPlayImageFile) in.readValue(MusicPlayImageFile.class.getClassLoader());
            data.systemTime = in.readLong();
            return data;
        }

        @Override
        public MainPageInfo[] newArray(int size) {
            return new MainPageInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enName);
        dest.writeString(speech);
        dest.writeString(zhName);
        dest.writeValue(fileUpload);
        dest.writeValue(logo);
        dest.writeString(customerName);
        dest.writeString(customerId);
        dest.writeString(id);
        dest.writeString(startingVideo);
        dest.writeValue(paymentSetting);
        dest.writeValue(adSetting);
        dest.writeValue(adSetting.mediaDetailImageFile);
        dest.writeValue(adSetting.mediaPlayImageFile);
        dest.writeValue(adSetting.musicPlayImageFile);
        dest.writeValue(systemTime);
    }

    public static class FileUpload implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Logo implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class PaymentSetting implements Serializable {
        private int dines;
        private int id;
        private int mall;
        private int payOnline;
        private int payWithRoomfee;

        public int getDines() {
            return dines;
        }

        public void setDines(int dines) {
            this.dines = dines;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMall() {
            return mall;
        }

        public void setMall(int mall) {
            this.mall = mall;
        }

        public int getPayOnline() {
            return payOnline;
        }

        public void setPayOnline(int payOnline) {
            this.payOnline = payOnline;
        }

        public int getPayWithRoomfee() {
            return payWithRoomfee;
        }

        public void setPayWithRoomfee(int payWithRoomfee) {
            this.payWithRoomfee = payWithRoomfee;
        }
    }

    public static class AdSetting implements Serializable {
        private int id;
        private int mainPage;//首页广告
        private int mediaDetailImage;//电影详情图片广告
        private int mediaDetailVideo;//电影详情视频广告
        private int mediaPlayImage;//电影暂停图片广告
        private int mediaPlayVideo15;//电影播放15秒穿插广告
        private int mediaPlayVideo30;//电影播放30秒开头广告
        private int musicPlayImage;//音乐图片广告

        private MediaDetailImageFile mediaDetailImageFile;//电影详情广告图
        private MediaPlayImageFile mediaPlayImageFile;//电影暂停图
        private MusicPlayImageFile musicPlayImageFile;//音乐播放广告图

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMainPage() {
            return mainPage;
        }

        public void setMainPage(int mainPage) {
            this.mainPage = mainPage;
        }

        public int getMediaDetailImage() {
            return mediaDetailImage;
        }

        public void setMediaDetailImage(int mediaDetailImage) {
            this.mediaDetailImage = mediaDetailImage;
        }

        public int getMediaDetailVideo() {
            return mediaDetailVideo;
        }

        public void setMediaDetailVideo(int mediaDetailVideo) {
            this.mediaDetailVideo = mediaDetailVideo;
        }

        public int getMediaPlayImage() {
            return mediaPlayImage;
        }

        public void setMediaPlayImage(int mediaPlayImage) {
            this.mediaPlayImage = mediaPlayImage;
        }

        public int getMediaPlayVideo15() {
            return mediaPlayVideo15;
        }

        public void setMediaPlayVideo15(int mediaPlayVideo15) {
            this.mediaPlayVideo15 = mediaPlayVideo15;
        }

        public int getMediaPlayVideo30() {
            return mediaPlayVideo30;
        }

        public void setMediaPlayVideo30(int mediaPlayVideo30) {
            this.mediaPlayVideo30 = mediaPlayVideo30;
        }

        public int getMusicPlayImage() {
            return musicPlayImage;
        }

        public void setMusicPlayImage(int musicPlayImage) {
            this.musicPlayImage = musicPlayImage;
        }

        public MediaDetailImageFile getMediaDetailImageFile() {
            return mediaDetailImageFile;
        }

        public void setMediaDetailImageFile(MediaDetailImageFile mediaDetailImageFile) {
            this.mediaDetailImageFile = mediaDetailImageFile;
        }

        public MediaPlayImageFile getMediaPlayImageFile() {
            return mediaPlayImageFile;
        }

        public void setMediaPlayImageFile(MediaPlayImageFile mediaPlayImageFile) {
            this.mediaPlayImageFile = mediaPlayImageFile;
        }

        public MusicPlayImageFile getMusicPlayImageFile() {
            return musicPlayImageFile;
        }

        public void setMusicPlayImageFile(MusicPlayImageFile musicPlayImageFile) {
            this.musicPlayImageFile = musicPlayImageFile;
        }
    }


    public static class MediaDetailImageFile implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class MediaPlayImageFile implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class MusicPlayImageFile implements Serializable {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartingVideo() {
        return startingVideo;
    }

    public void setStartingVideo(String startingVideo) {
        this.startingVideo = startingVideo;
    }

    public PaymentSetting getPaymentSetting() {
        return paymentSetting;
    }

    public void setPaymentSetting(PaymentSetting paymentSetting) {
        this.paymentSetting = paymentSetting;
    }

    public AdSetting getAdSetting() {
        return adSetting;
    }

    public void setAdSetting(AdSetting adSetting) {
        this.adSetting = adSetting;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }

    @Override
    public String toString() {
        return "MainPageInfo{" +
                "id='" + id + '\'' +
                ", enName='" + enName + '\'' +
                ", speech='" + speech + '\'' +
                ", zhName='" + zhName + '\'' +
                ", fileUpload=" + fileUpload +
                ", logo=" + logo +
                ", customerName='" + customerName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", startingVideo='" + startingVideo + '\'' +
                ", paymentSetting=" + paymentSetting + '\'' +
                ", adSetting=" + adSetting +
                '}';
    }
}
