package com.yht.iptv.model;

import java.util.List;

/**
 * Created by admin on 2017/8/12.
 */

public class MallGoodsListInfo {


    private List<GoodsListBean> goods_list;
    private int total;

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class GoodsListBean {
        /**
         * bargain : 0
         * description :
         * hasoption : 0
         * id : 230
         * isdiscount : 0
         * isdiscount_time : 1500083160
         * ispresell : 0
         * marketprice : 998.00
         * maxprice : 998.00
         * minprice : 998.00
         * productprice : 1799.00
         * sales : 1
         * salesreal : 0
         * thumb : http://10.0.10.108/attachment/images/1/2017/07/iW34vwbZt4sBI7aW72F71tvUtAt1lw.jpg
         * title : 正山小种
         * total : 20
         * type : 1
         */

        private String bargain;
        private String description;
        private String hasoption;
        private String id;
        private String isdiscount;
        private String isdiscount_time;
        private String ispresell;
        private String marketprice;
        private String maxprice;
        private String minprice;
        private String productprice;
        private String sales;
        private String salesreal;
        private String thumb;
        private String title;
        private String total;
        private String type;

        public String getBargain() {
            return bargain;
        }

        public void setBargain(String bargain) {
            this.bargain = bargain;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHasoption() {
            return hasoption;
        }

        public void setHasoption(String hasoption) {
            this.hasoption = hasoption;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsdiscount() {
            return isdiscount;
        }

        public void setIsdiscount(String isdiscount) {
            this.isdiscount = isdiscount;
        }

        public String getIsdiscount_time() {
            return isdiscount_time;
        }

        public void setIsdiscount_time(String isdiscount_time) {
            this.isdiscount_time = isdiscount_time;
        }

        public String getIspresell() {
            return ispresell;
        }

        public void setIspresell(String ispresell) {
            this.ispresell = ispresell;
        }

        public String getMarketprice() {
            return marketprice;
        }

        public void setMarketprice(String marketprice) {
            this.marketprice = marketprice;
        }

        public String getMaxprice() {
            return maxprice;
        }

        public void setMaxprice(String maxprice) {
            this.maxprice = maxprice;
        }

        public String getMinprice() {
            return minprice;
        }

        public void setMinprice(String minprice) {
            this.minprice = minprice;
        }

        public String getProductprice() {
            return productprice;
        }

        public void setProductprice(String productprice) {
            this.productprice = productprice;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getSalesreal() {
            return salesreal;
        }

        public void setSalesreal(String salesreal) {
            this.salesreal = salesreal;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
