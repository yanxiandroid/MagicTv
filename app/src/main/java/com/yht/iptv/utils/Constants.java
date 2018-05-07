package com.yht.iptv.utils;

import com.yht.iptv.model.MainPageInfo;

/**
 * Created by admin on 2017/10/16.
 */

public class Constants {


    public static final String IP_ADDRESS = "ip_address";
    public static final String ROOM_ID = "ROOM_ID";

    public static final String SIGNCHOOSE = "signChoose";//信号选择

    public static final String PAY_CHOOSE = "PAY_CHOOSE";//信号选择

    public static final String ONLINE_PAY = "ONLINE_PAY";//在线支付
    public static final String PMS_PAY = "PMS_PAY";//房账支付
    public static final String ALL_PAY = "ALL_PAY";//两者都支持

    public static final String HOTEL_FOOD_ID = "品美食";

    //按钮状态
    public static final String CONFIRM = "confirm";
    public static final String CANCEL = "cancel";
    public static final String THIRD = "THIRD";

    public static final String ROOM_INFO = "iptv_room";
    public static final String IP_INFO = "iptv_IPAddress";
    public static long REAL_TIME = 0;

    public static String packageSettings = "com.android.settings";

    public static long moviePosition = 0;
    public static long movieAdvPosition = 0;


    public static final Object DEMAND_Deail_TAG = "DEMAND_Deail_TAG";
    public static String CACHE_VIDEO = "CACHE_VIDEO";

    public static final String WEATHER_URL = "images/weather/";

    public static MainPageInfo mainPageInfo;

    public static final String MODULE_TITLE = "MODULE_TITLE";

    public static final String TITLE_SERVICE = "TITLE_SERVICE";
    public static final String TITLE_MOVIE = "TITLE_MOVIE";
    public static final String TITLE_SHOPPING = "TITLE_SHOPPING";
    public static final String TITLE_MUSIC = "TITLE_MUSIC";
    public static final String TITLE_TV = "TITLE_TV";
    public static final String TITLE_FOOD = "TITLE_FOOD";
    public static final String TITLE_NEAR = "TITLE_NEAR";
    public static final String TITLE_WEATHER = "TITLE_WEATHER";
    public static final String TITLE_MESSAGE = "TITLE_MESSAGE";
    public static final String TITLE_OTHER = "TITLE_OTHER";

    public static String mstar_tv_player = "com.mstar.tv.tvplayer.ui";

    //类型
    public static final int AUDIO_BACKGROUND = 444;
    public static final int AUDIO_LIST = 555;
    public static final int AUDIO_STATUS = 666;


    public static String currentLanguage = "zh";

    public static String verticalscrolltext = "food";

    public static String IP_VALUE = "";

    public static boolean SYSTEM_ALERT = false;

    //ip地址校验码
    public static final String CHECKIP = "HYZNIPTV";


    //视频浏览类型
    public static final String PAGE_STATUS = "PAGE_STATUS";
    public static final byte MOVIE_START = 0x10;
    public static final byte MOVIE_PAULE = 0x20;
    public static final byte MOVIE_RESUME = 0x30;
    public static final byte MOVIE_STOP = 0x40;
    public static final byte PAGE_START = 0x50;
    public static final byte PAGE_END = 0x60;

    public static final String MOVIE = "1";//电影
    public static final String SHOPPING = "2";//商城
    public static final String MUSIC = "3";//音乐
    public static final String TV = "4";//电视
    public static final String CATERING = "5";//餐饮

    public static final String WEATHER = "6";//天气
    public static final String MESSAGE = "7";//消息
    public static final String INTRODUCTION = "8";//酒店介绍
    public static final String PROTOCOL = "9";//礼宾
    public static final String MAKELOVE = "10";//康乐
    public static final String RESERVE = "11";//预定
    public static final String GUESTROOM = "12";//客房

    public static final String BUS = "13";//大巴
    public static final String METROMAP = "14";//地铁图
    public static final String PLACES = "15";//周边景点
    public static final String FOOD = "16";//周边餐饮
    public static final String SHOP = "17";//周边商城
    public static final String SPECIALTY = "18";//周边特产


    public static final int NORMAL_EXIT = 1;//正常
    public static final int ERROR_EXIT = 2;//错误
    public static final int TIMEOUT_EXIT = 3;//超时
    public static final int SOCKET_CHANGE = 4;//投影切换
    public static final int PAY_CANCEL = 5;//支付取消退出

    public static final int PREVIEW = 1;//试看
    public static final int ONCE_PLAY = 2;//单片
    public static final int ALL_PLAY = 3;//包日
    public static final int PAY_PLAY = 4;//已付费观看
    public static final int SOCKET_PLAY = 5;//投影

    public static final String NETWORK_OK = "NETWORK_OK";
    public static final String NETWORK_ERROR = "NETWORK_ERROR";

    public static final String[] key = {"first", "second"};
}
