package com.yht.iptv.presenter;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.utils.AppUtils;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */

public class PageRecordPresenter {



        private IPresenterBase<List<Long>> iPresenterBase;

        public PageRecordPresenter(IPresenterBase<List<Long>> iPresenterBase) {
            this.iPresenterBase = iPresenterBase;
        }
        public PageRecordPresenter() {
        }

        /**
         * 页面记录
         * @param roomNum
         * @param behaviour
         */
        public void pageStart(String roomNum, String behaviour){

            HttpParams params = new HttpParams();
            params.put("roomNum",roomNum);
            params.put("behaviour",behaviour);
            params.put("platform","1");
            params.put("version","V" + AppUtils.getAppVersionName() + "_" + AppUtils.getAppVersionCode());
            OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.PAGE_RECORD, params, new JsonCallback<BaseModel<List<Long>>>() {
                @Override
                public void onSuccess(Response<BaseModel<List<Long>>> response) {
                    BaseModel<List<Long>> body = response.body();
                    List<Long> data = body.data;
                    if(data != null && data.size() > 0) {
                        iPresenterBase.onSuccess(body);
                    }else{
                        iPresenterBase.onError();
                    }
                }
            });
        }


        /**
         * 页面结束记录
         * @param id
         */
        public void pageEnd(long id){
            HttpParams params = new HttpParams();
            params.put("id",id);
            OkHttpUtils.postJson(HttpConstants.HTTP_HOST + HttpConstants.PAGE_END_RECORD, params, new JsonCallback<BaseModel<List<Long>>>() {
                @Override
                public void onSuccess(Response<BaseModel<List<Long>>> response) {

                }
            });
        }
}
