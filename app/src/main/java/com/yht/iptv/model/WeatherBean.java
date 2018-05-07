package com.yht.iptv.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/6/19.
 * 天气的本地封装
 */

public class WeatherBean implements Serializable{


    /**
     * id : 22
     * dateTime : 2017-06-19 12:00:00
     * result : {"HeWeather5":[{"aqi":{"city":{"aqi":"21","co":"1","no2":"38","o3":"30","pm10":"21","pm25":"11","qlty":"优","so2":"6"}},"basic":{"city":"深圳","cnty":"中国","id":"CN101280601","lat":"22.54700089","lon":"114.08594513","update":{"loc":"2017-06-19 11:50","utc":"2017-06-19 03:50"}},"daily_forecast":[{"astro":{"mr":"01:28","ms":"14:02","sr":"05:40","ss":"19:10"},"cond":{"code_d":"307","code_n":"310","txt_d":"大雨","txt_n":"暴雨"},"date":"2017-06-19","hum":"84","pcpn":"24.5","pop":"100","pres":"1004","tmp":{"max":"29","min":"25"},"uv":"2","vis":"14","wind":{"deg":"192","dir":"无持续风向","sc":"微风","spd":"8"}},{"astro":{"mr":"02:10","ms":"15:02","sr":"05:40","ss":"19:10"},"cond":{"code_d":"307","code_n":"310","txt_d":"大雨","txt_n":"暴雨"},"date":"2017-06-20","hum":"83","pcpn":"4.0","pop":"100","pres":"1004","tmp":{"max":"30","min":"26"},"uv":"9","vis":"14","wind":{"deg":"200","dir":"无持续风向","sc":"微风","spd":"8"}},{"astro":{"mr":"02:56","ms":"16:05","sr":"05:40","ss":"19:10"},"cond":{"code_d":"307","code_n":"307","txt_d":"大雨","txt_n":"大雨"},"date":"2017-06-21","hum":"82","pcpn":"9.2","pop":"100","pres":"1004","tmp":{"max":"30","min":"27"},"uv":"8","vis":"14","wind":{"deg":"196","dir":"无持续风向","sc":"微风","spd":"3"}}],"hourly_forecast":[{"cond":{"code":"301","txt":"强阵雨"},"date":"2017-06-19 13:00","hum":"79","pop":"99","pres":"1004","tmp":"28","wind":{"deg":"206","dir":"西南风","sc":"3-4","spd":"17"}},{"cond":{"code":"305","txt":"小雨"},"date":"2017-06-19 16:00","hum":"79","pop":"97","pres":"1003","tmp":"28","wind":{"deg":"206","dir":"西南风","sc":"3-4","spd":"18"}},{"cond":{"code":"300","txt":"阵雨"},"date":"2017-06-19 19:00","hum":"81","pop":"97","pres":"1004","tmp":"28","wind":{"deg":"203","dir":"西南风","sc":"微风","spd":"12"}},{"cond":{"code":"305","txt":"小雨"},"date":"2017-06-19 22:00","hum":"83","pop":"95","pres":"1005","tmp":"27","wind":{"deg":"191","dir":"西南风","sc":"微风","spd":"12"}}],"now":{"cond":{"code":"300","txt":"阵雨"},"fl":"35","hum":"89","pcpn":"0","pres":"1006","tmp":"28","vis":"10","wind":{"deg":"200","dir":"南风","sc":"3-4","spd":"12"}},"status":"ok","suggestion":{"comf":{"brf":"较不舒适","txt":"白天虽然有雨，但仍无法削弱较高气温带来的暑意，同时降雨造成湿度加大会您感到有些闷热，不很舒适。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"热","txt":"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"},"flu":{"brf":"较易发","txt":"天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。"},"sport":{"brf":"较不宜","txt":"有较强降水，建议您选择在室内进行健身休闲运动。"},"trav":{"brf":"较不宜","txt":"温度适宜，风力不大，但预计将有有强降水出现，会给您的出游增添很多麻烦，建议您最好选择室内活动。"},"uv":{"brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"}}}]}
     */

    private int id;
    private String dateTime;
    private String result;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
