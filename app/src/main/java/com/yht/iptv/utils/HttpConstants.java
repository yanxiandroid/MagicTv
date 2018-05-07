package com.yht.iptv.utils;

/**
 * Created by admin on 2016/5/25.
 */
public class HttpConstants {
    //public static final String HTTP_HOST = "http://10.0.10.227:8080/";
    //public static final String HTTP_HOST = "http://10.0.10.105:8080/";
    public static String HTTP_HOST;
    public static String HTTP_MALL_HOST = "http://www.hyzc.ltd/";
    //    public static String HTTP_MALL_HOST="http://10.0.10.108/";
    public static String HTTP_HOST_TEST = "http://10.0.10.165:8080/";
//    public static final String HTTP_HOST = "http://10.0.10.105:8080/";

    public static final String MOVIE_CLASSIFICATION = "iptv.api/category/getAllList";
    public static final String MOVIE_TYPE = "iptv.api/type/getAllList";
    public static final String MOVIE_DETAIL = "iptv.api/video/getListByPage";
    public static final String MOVIE_RECORD = "iptv.api/video/count";
    public static final String MOVIE_ADVERTISING = "iptv.api/advertising/getList";
    public static final String MOVIE_RECOMMEND = "/iptv.api/video/recommend";
    public static final String HOTEL_INTRODUCE = "/iptv.api/introduce/getAllList";
    public static final String HOTEL_PHONE = "/iptv.api/directories/getListByPage";
    public static final String HOTEL_VPIGIFT = "/iptv.api/gift/getAllList";
    public static final String HOTEL_FOOD = "/iptv.api/restaurant/getAllList";
    public static final String HOTEL_COOK = "/iptv.api/chef/getAllList";
    public static final String HOTEL_FOOD_TYPE = "/iptv.api/dinesMenu/getTypeByRestaurantId";
    public static final String HOTEL_FOOD_LIST = "/iptv.api/dinesMenu/getListByCondition";
    public static final String HOTEL_CAR = "/iptv.api/etiquette/getAllList";
    public static final String HOTEL_MEETING = "/iptv.api/reserve/getAllList";
    public static final String HOTEL_MAKELOVE = "/iptv.api/recreationType/getAllList";
    public static final String NEAR_BUS = "/iptv.api/bus/getList";
    public static final String NEAR_FOOD = "/iptv.api/food/getAllList";
    public static final String NEAR_SHOP = "/iptv.api/market/getAllList";
    public static final String NEAR_PLACES = "/iptv.api/scenic/getAllList";
    public static final String NEAR_SPECIALTY = "/iptv.api/specialty/getAllList";
    public static final String NEAR_SUBWAY = "/iptv.api/metro/getAllList";
    public static final String HOTEL_MAKELOVE_ITEM = "/iptv.api/recreation/getList";
    public static final String MAIN_PAGE_INFO = "/iptv.api/hotelInfo/getAllList";
    public static final String APP_DOWNLOAD = "/iptv.api/app/get";
    public static final String APP_RECORD = "/iptv.api/hotelServiceLog/save";
    public static final String APP_RECORD_NEAR = "iptv.api/hotelPeripheryLog/save";
    public static final String ORDER_APPLY = "iptv.api/order/save";
    public static final String ORDER_LIST = "iptv.api/order/getList";
    public static final String HOTEL_ROOM_SERVICE = "iptv.api/guestRoomServer/save";
    public static final String DUE_LIST = "iptv.api/guestRoomServer/getList";
    public static final String GIFT_TYPE = "iptv.api/gift/getAllTypeList";
    public static final String GIFT_DETAIL = "iptv.api/gift/getList";
    public static final String EXCEPTION_INFO = "iptv.api/exception/save";
    public static final String MUSIC_LIST = "iptv.api/music/type/getAllList";
    public static final String MUSIC_DETAIL = "iptv.api/music/getList";
    public static final String HOTEL_WASHING_PRICE = "iptv.api/laundry/getList";
    public static final String HOTEL_DUE_CANCEL = "iptv.api/guestRoomServer/updateByPhone";
    public static final String WEATHER_INFO = "iptv.api/weather/getWeather";
    public static final String GET_PAY_QRCODE = "iptv.api/videoOrder/createOrder";
    public static final String GET_PAY_STATUS = "iptv.api/videoOrder/checkOrderStatus";
    public static final String GET_FOOD_PAY_QRCODE = "iptv.api/dinesOrder/createOrder";
    public static final String GET_FOOD_PAY_STATUS = "iptv.api/dinesOrder/checkOrder";
    public static final String MOVIE_START_RECORD = "iptv.api/video/saveBeginning";
    public static final String MOVIE_STOP_RECORD = "iptv.api/video/saveEnding";
    public static final String MOVIE_PAUSE_RECORD = "iptv.api/video/pauseLogging";
    public static final String MOVIE_RESUME_RECORD = "iptv.api/video/continueLogging";
    public static final String PAGE_RECORD = "iptv.api/behaviourLog/saveBeginning";
    public static final String PAGE_END_RECORD = "iptv.api/behaviourLog/saveEnding";
    public static final String PUSH_MESSAGE = "iptv.api/pushInfo/list";
    public static final String FOOD_ORDER = "iptv.api/dinesOrder/PMSOrder";
    public static final String ACCOUNT_PAY = "iptv.api/videoOrder/payWithRoomFee";
    public static final String ACCOUNT_PAY_DINES = "iptv.api/dinesOrder/payWithRoomFee";
    public static final String MAIN_NAVIGATION = "iptv.api/serviceSetting/listAll";



    public static final String ADVERTVIDEO = "iptv.api/ad/video/display";
    public static final String IMAGE_DISPLAY = "iptv.api/ad/image/display";
    public static final String AD_STATISTICS = "iptv.api/ad/log/count";

    public static final String MALL_GOODS_TITLE = "wxapi/index.php?r=shop.category&i=3";
    public static final String MALL_GOODS_LIST = "wxapi/index.php?r=goods&i=3";
    public static final String MALL_GOODS_DETAIL = "wxapi/index.php?r=goods.detail&i=3";
    public static final String MALL_GOODS_ORDER_DETAIL = "wxapi/index.php?r=member.cart.add";


    public static final String MALL_GOODS_LOGIN_INFO = "http://www.hyzc.ltd/app/index.php?i=3&c=entry&m=ewei_shopv2&do=mobile";
    public static final String MALL_GOODS_LOGIN_STATUS = "http://www.hyzc.ltd/wxapi/index.php";


}
