package com.yht.iptv.model;

import java.util.List;

/**
 * Created by admin on 2017/8/12.
 */

public class MallGoodsDetailInfo {


    /**
     * goods : {"goods_id":"211","uniacid":"1","cash":"0","quality":"0","seven":"0","invoice":"0","repair":"0","cat_id1":0,"cat_id2":0,"cat_id3":0,"store_cat_id1":0,"store_cat_id2":0,"goods_sn":"","goods_name":"9000mAh快充快放\u2014移动电源","click_count":"182","brand_id":0,"store_count":"400","collect_sum":"182","comment_count":19,"weight":"9000.00","market_price":166.8,"shop_price":"139.00","cost_price":"100.00","exchange_integral":0,"keywords":"","goods_remark":"","goods_content":"<p><span style=\"background-color: rgb(255, 255, 255);\">&nbsp;&nbsp;<\/span><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448216901517.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 764px; height: 424px;\"/><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448224323677.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 763px; height: 781px;\"/><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448225524895.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 736px;\"/><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448226081557.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 868px;\"/><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448229763339.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 608px;\"/><img src=\"http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448231648177.jpg\" style=\"margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 723px;\"/><span style=\"background-color: rgb(255, 255, 255);\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<\/span><\/p>","original_img":"http://10.0.10.108/attachment/images/1/2017/05/TNIIxIITWIxikNpPtU5XUGAb5R55bA.jpg","is_real":"1","is_on_sale":true,"is_free_shipping":"0","on_time":0,"sort":"2","is_recommend":"1","is_new":"1","is_hot":"1","last_update":0,"goods_type":0,"give_integral":"","sales_sum":19,"prom_type":"0","prom_id":0,"distribut":0,"store_id":"1","spu":0,"sku":0,"goods_state":0,"suppliers_id":0,"dispatchprice":"10.00","goods_attr_list":[],"goods_spec_list":[[{"spec_name":"颜色","item_id":"328","item":"白色","src":"","isClick":1},{"spec_name":"颜色","item_id":"329","item":"黑色","src":"","isClick":0}],[{"spec_name":"尺寸","item_id":"330","item":"170","src":"","isClick":1},{"spec_name":"尺寸","item_id":"331","item":"180","src":"","isClick":0}]]}
     * spec_goods_price : [{"id":"436","key":"328_330","price":"100.00","store_count":"100"},{"id":"438","key":"329_330","price":"100.00","store_count":"100"},{"id":"437","key":"328_331","price":"100.00","store_count":"100"},{"id":"439","key":"329_331","price":"100.00","store_count":"100"}]
     * gallery : [{"image_url":"http://10.0.10.108/attachment/images/1/2017/05/TNIIxIITWIxikNpPtU5XUGAb5R55bA.jpg"}]
     * goods_attr_list : []
     * comment : []
     * getComments : true
     * isfavorite : 0
     * qrcode : http://10.0.10.108/addons/ewei_shopv2/data/qrcode/1/3fd842a3f602d094e52e5f24abf1ea3d.jpg
     */

    private GoodsBean goods;
    private boolean getComments;
    private int isfavorite;
    private String qrcode;
    private List<SpecGoodsPriceBean> spec_goods_price;
    private List<GalleryBean> gallery;
    private List<?> goods_attr_list;
    private List<?> comment;

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public boolean isGetComments() {
        return getComments;
    }

    public void setGetComments(boolean getComments) {
        this.getComments = getComments;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<SpecGoodsPriceBean> getSpec_goods_price() {
        return spec_goods_price;
    }

    public void setSpec_goods_price(List<SpecGoodsPriceBean> spec_goods_price) {
        this.spec_goods_price = spec_goods_price;
    }

    public List<GalleryBean> getGallery() {
        return gallery;
    }

    public void setGallery(List<GalleryBean> gallery) {
        this.gallery = gallery;
    }

    public List<?> getGoods_attr_list() {
        return goods_attr_list;
    }

    public void setGoods_attr_list(List<?> goods_attr_list) {
        this.goods_attr_list = goods_attr_list;
    }

    public List<?> getComment() {
        return comment;
    }

    public void setComment(List<?> comment) {
        this.comment = comment;
    }

    public static class GoodsBean {
        /**
         * goods_id : 211
         * uniacid : 1
         * cash : 0
         * quality : 0
         * seven : 0
         * invoice : 0
         * repair : 0
         * cat_id1 : 0
         * cat_id2 : 0
         * cat_id3 : 0
         * store_cat_id1 : 0
         * store_cat_id2 : 0
         * goods_sn :
         * goods_name : 9000mAh快充快放—移动电源
         * click_count : 182
         * brand_id : 0
         * store_count : 400
         * collect_sum : 182
         * comment_count : 19
         * weight : 9000.00
         * market_price : 166.8
         * shop_price : 139.00
         * cost_price : 100.00
         * exchange_integral : 0
         * keywords :
         * goods_remark :
         * goods_content : <p><span style="background-color: rgb(255, 255, 255);">&nbsp;&nbsp;</span><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448216901517.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 764px; height: 424px;"/><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448224323677.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 763px; height: 781px;"/><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448225524895.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 736px;"/><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448226081557.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 868px;"/><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448229763339.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 608px;"/><img src="http://brnshop.yitucehui.com/upload/product/editor/pe_1704101448231648177.jpg" style="margin: 0px; padding: 0px; border: 0px none; white-space: normal; background-color: rgb(255, 255, 255); width: 788px; height: 723px;"/><span style="background-color: rgb(255, 255, 255);">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span></p>
         * original_img : http://10.0.10.108/attachment/images/1/2017/05/TNIIxIITWIxikNpPtU5XUGAb5R55bA.jpg
         * is_real : 1
         * is_on_sale : true
         * is_free_shipping : 0
         * on_time : 0
         * sort : 2
         * is_recommend : 1
         * is_new : 1
         * is_hot : 1
         * last_update : 0
         * goods_type : 0
         * give_integral :
         * sales_sum : 19
         * prom_type : 0
         * prom_id : 0
         * distribut : 0
         * store_id : 1
         * spu : 0
         * sku : 0
         * goods_state : 0
         * suppliers_id : 0
         * dispatchprice : 10.00
         * goods_attr_list : []
         * goods_spec_list : [[{"spec_name":"颜色","item_id":"328","item":"白色","src":"","isClick":1},{"spec_name":"颜色","item_id":"329","item":"黑色","src":"","isClick":0}],[{"spec_name":"尺寸","item_id":"330","item":"170","src":"","isClick":1},{"spec_name":"尺寸","item_id":"331","item":"180","src":"","isClick":0}]]
         */

        private String goods_id;
        private String uniacid;
        private String cash;
        private String quality;
        private String seven;
        private String invoice;
        private String repair;
        private int cat_id1;
        private int cat_id2;
        private int cat_id3;
        private int store_cat_id1;
        private int store_cat_id2;
        private String goods_sn;
        private String goods_name;
        private String click_count;
        private int brand_id;
        private String store_count;
        private String collect_sum;
        private int comment_count;
        private String weight;
        private String market_price;
        private String shop_price;
        private String cost_price;
        private int exchange_integral;
        private String keywords;
        private String goods_remark;
        private String goods_content;
        private String original_img;
        private String is_real;
        private boolean is_on_sale;
        private String is_free_shipping;
        private int on_time;
        private String sort;
        private String is_recommend;
        private String is_new;
        private String is_hot;
        private int last_update;
        private int goods_type;
        private String give_integral;
        private int sales_sum;
        private String prom_type;
        private int prom_id;
        private int distribut;
        private String store_id;
        private int spu;
        private int sku;
        private int goods_state;
        private int suppliers_id;
        private String dispatchprice;
        private List<?> goods_attr_list;
        private List<List<GoodsSpecListBean>> goods_spec_list;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getUniacid() {
            return uniacid;
        }

        public void setUniacid(String uniacid) {
            this.uniacid = uniacid;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getSeven() {
            return seven;
        }

        public void setSeven(String seven) {
            this.seven = seven;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getRepair() {
            return repair;
        }

        public void setRepair(String repair) {
            this.repair = repair;
        }

        public int getCat_id1() {
            return cat_id1;
        }

        public void setCat_id1(int cat_id1) {
            this.cat_id1 = cat_id1;
        }

        public int getCat_id2() {
            return cat_id2;
        }

        public void setCat_id2(int cat_id2) {
            this.cat_id2 = cat_id2;
        }

        public int getCat_id3() {
            return cat_id3;
        }

        public void setCat_id3(int cat_id3) {
            this.cat_id3 = cat_id3;
        }

        public int getStore_cat_id1() {
            return store_cat_id1;
        }

        public void setStore_cat_id1(int store_cat_id1) {
            this.store_cat_id1 = store_cat_id1;
        }

        public int getStore_cat_id2() {
            return store_cat_id2;
        }

        public void setStore_cat_id2(int store_cat_id2) {
            this.store_cat_id2 = store_cat_id2;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getClick_count() {
            return click_count;
        }

        public void setClick_count(String click_count) {
            this.click_count = click_count;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public String getStore_count() {
            return store_count;
        }

        public void setStore_count(String store_count) {
            this.store_count = store_count;
        }

        public String getCollect_sum() {
            return collect_sum;
        }

        public void setCollect_sum(String collect_sum) {
            this.collect_sum = collect_sum;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getCost_price() {
            return cost_price;
        }

        public void setCost_price(String cost_price) {
            this.cost_price = cost_price;
        }

        public int getExchange_integral() {
            return exchange_integral;
        }

        public void setExchange_integral(int exchange_integral) {
            this.exchange_integral = exchange_integral;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getGoods_remark() {
            return goods_remark;
        }

        public void setGoods_remark(String goods_remark) {
            this.goods_remark = goods_remark;
        }

        public String getGoods_content() {
            return goods_content;
        }

        public void setGoods_content(String goods_content) {
            this.goods_content = goods_content;
        }

        public String getOriginal_img() {
            return original_img;
        }

        public void setOriginal_img(String original_img) {
            this.original_img = original_img;
        }

        public String getIs_real() {
            return is_real;
        }

        public void setIs_real(String is_real) {
            this.is_real = is_real;
        }

        public boolean isIs_on_sale() {
            return is_on_sale;
        }

        public void setIs_on_sale(boolean is_on_sale) {
            this.is_on_sale = is_on_sale;
        }

        public String getIs_free_shipping() {
            return is_free_shipping;
        }

        public void setIs_free_shipping(String is_free_shipping) {
            this.is_free_shipping = is_free_shipping;
        }

        public int getOn_time() {
            return on_time;
        }

        public void setOn_time(int on_time) {
            this.on_time = on_time;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(String is_hot) {
            this.is_hot = is_hot;
        }

        public int getLast_update() {
            return last_update;
        }

        public void setLast_update(int last_update) {
            this.last_update = last_update;
        }

        public int getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(int goods_type) {
            this.goods_type = goods_type;
        }

        public String getGive_integral() {
            return give_integral;
        }

        public void setGive_integral(String give_integral) {
            this.give_integral = give_integral;
        }

        public int getSales_sum() {
            return sales_sum;
        }

        public void setSales_sum(int sales_sum) {
            this.sales_sum = sales_sum;
        }

        public String getProm_type() {
            return prom_type;
        }

        public void setProm_type(String prom_type) {
            this.prom_type = prom_type;
        }

        public int getProm_id() {
            return prom_id;
        }

        public void setProm_id(int prom_id) {
            this.prom_id = prom_id;
        }

        public int getDistribut() {
            return distribut;
        }

        public void setDistribut(int distribut) {
            this.distribut = distribut;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public int getSpu() {
            return spu;
        }

        public void setSpu(int spu) {
            this.spu = spu;
        }

        public int getSku() {
            return sku;
        }

        public void setSku(int sku) {
            this.sku = sku;
        }

        public int getGoods_state() {
            return goods_state;
        }

        public void setGoods_state(int goods_state) {
            this.goods_state = goods_state;
        }

        public int getSuppliers_id() {
            return suppliers_id;
        }

        public void setSuppliers_id(int suppliers_id) {
            this.suppliers_id = suppliers_id;
        }

        public String getDispatchprice() {
            return dispatchprice;
        }

        public void setDispatchprice(String dispatchprice) {
            this.dispatchprice = dispatchprice;
        }

        public List<?> getGoods_attr_list() {
            return goods_attr_list;
        }

        public void setGoods_attr_list(List<?> goods_attr_list) {
            this.goods_attr_list = goods_attr_list;
        }

        public List<List<GoodsSpecListBean>> getGoods_spec_list() {
            return goods_spec_list;
        }

        public void setGoods_spec_list(List<List<GoodsSpecListBean>> goods_spec_list) {
            this.goods_spec_list = goods_spec_list;
        }

        public static class GoodsSpecListBean {
            /**
             * spec_name : 颜色
             * item_id : 328
             * item : 白色
             * src :
             * isClick : 1
             */

            private String spec_name;
            private String item_id;
            private String item;
            private String src;
            private int isClick;

            public String getSpec_name() {
                return spec_name;
            }

            public void setSpec_name(String spec_name) {
                this.spec_name = spec_name;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getItem() {
                return item;
            }

            public void setItem(String item) {
                this.item = item;
            }

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public int getIsClick() {
                return isClick;
            }

            public void setIsClick(int isClick) {
                this.isClick = isClick;
            }
        }
    }

    public static class SpecGoodsPriceBean {
        /**
         * id : 436
         * key : 328_330
         * price : 100.00
         * store_count : 100
         */

        private String id;
        private String key;
        private String price;
        private String store_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStore_count() {
            return store_count;
        }

        public void setStore_count(String store_count) {
            this.store_count = store_count;
        }
    }

    public static class GalleryBean {
        /**
         * image_url : http://10.0.10.108/attachment/images/1/2017/05/TNIIxIITWIxikNpPtU5XUGAb5R55bA.jpg
         */

        private String image_url;

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
