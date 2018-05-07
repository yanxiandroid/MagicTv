package com.yht.iptv.presenter;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yht.iptv.callback.IPresenterDownloadBase;
import com.yht.iptv.callback.JsonCallback;
import com.yht.iptv.model.AdvertVideoInfo;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.utils.Constants;
import com.yht.iptv.utils.FileUtils;
import com.yht.iptv.utils.HttpConstants;
import com.yht.iptv.utils.OkHttpUtils;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * Created by admin on 2018/1/5.
 */

public class AdvertDownloadPresenter {

    private String currentFileName;
    private IPresenterDownloadBase<List<AdvertVideoInfo>> iPresenterBase;

    public AdvertDownloadPresenter(IPresenterDownloadBase<List<AdvertVideoInfo>> iPresenterBase) {
        this.iPresenterBase = iPresenterBase;
    }
    public AdvertDownloadPresenter(String currentFileName,IPresenterDownloadBase<List<AdvertVideoInfo>> iPresenterBase) {
        this.currentFileName = currentFileName;
        this.iPresenterBase = iPresenterBase;
    }

    public void request(final Object tag, final int length,String roomNum){
        HttpParams params = new HttpParams();
        params.put("length",length);
        params.put("roomNum",roomNum);
        OkHttpUtils.getJson(HttpConstants.HTTP_HOST + HttpConstants.ADVERTVIDEO, tag, params, new JsonCallback<BaseModel<List<AdvertVideoInfo>>>() {
            @Override
            public void onSuccess(Response<BaseModel<List<AdvertVideoInfo>>> response) {
                try {
                    Log.e("download_success","请求下载的:"+response.body().data.get(0).getFileUpload().getPath());
                    AdvertVideoInfo advertVideoInfo = response.body().data.get(0);
                    String filePath;
                    if(length == 15) {
                        filePath = FileUtils.getAdvert15Path();
                    }else{
                        filePath = FileUtils.getAdvertPath();
                    }
                    String fileName = null;
                    if(FileUtils.fileNumber(filePath) == 0){
                        fileName = Constants.key[0];
                        Log.e("download_success","1:" + "000000");
                    }else if(FileUtils.fileNumber(filePath) == 1){
                        File[] files = FileUtils.fileVideo(filePath);
                        if(files != null) {
                            String absolutePath = files[0].getAbsolutePath();
                            //得到名字
                            String substring = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                            //是MP4文件
                            if(substring.substring(substring.lastIndexOf(".") + 1).equals("mp4")){
                                Log.e("download_success","1:" + substring);
                                for(int i = 0 ;i<Constants.key.length;i++){
                                    if(substring.equals(Constants.key[i]+".mp4")){
                                        fileName = Constants.key[(i+1) % Constants.key.length];
                                    }
                                }
                            }else{
                                //不是MP4文件是损坏的文件
                                fileName = substring;

                            }
                        }
                        Log.e("download_success","1:" + "111111");
                    }else if(FileUtils.fileNumber(filePath) == 2){
                        if(currentFileName == null || currentFileName.equals("")){
                            fileName = Constants.key[0];
                        }else{
                            File[] files = FileUtils.fileVideo(filePath);
                            Log.e("download_success","2:"+currentFileName);
                            if(files != null) {
                                for(int i = 0 ; i < files.length;i++){
                                    if(currentFileName.equals(files[i].getAbsolutePath())){
                                        //得到名字
                                        String substring = currentFileName.substring(currentFileName.lastIndexOf("/") + 1);
                                        Log.e("download_success","2:" + substring);
                                        for(int j = 0 ;j<Constants.key.length;j++){
                                            if(substring.equals(Constants.key[j]+".mp4")){
                                                fileName = Constants.key[(j+1) % Constants.key.length];
                                                break;
                                            }
                                        }
                                        //损坏的文件
                                        if(fileName == null){
                                            fileName = substring;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        Log.e("download_success","1:" + "222222");
                    }else{
                        if(currentFileName == null || currentFileName.equals("")){
                            fileName = Constants.key[0];
                        }else {
                            File[] files = FileUtils.fileVideo(filePath);
                            for(int i = 0 ; i < files.length;i++){
                                if(currentFileName.equals(files[i].getAbsolutePath())){
                                    //得到名字
                                    String substring = currentFileName.substring(currentFileName.lastIndexOf("/") + 1);
                                    Log.e("download_success","2:" + substring);
                                    for(int j = 0 ;j<Constants.key.length;j++){
                                        if(substring.equals(Constants.key[j]+".mp4")){
                                            fileName = Constants.key[(j+1) % Constants.key.length];
                                            break;
                                        }
                                    }
                                    //损坏的文件
                                    if(fileName == null){
                                        fileName = substring;
                                    }
                                    break;
                                }
                            }
                        }
                        Log.e("download_success","1:" + "333333");
                    }
                    iPresenterBase.onSuccess(response.body());
                    download(advertVideoInfo.getFileUpload().getPath(), filePath,fileName);
                }catch (Exception e){
                    iPresenterBase.onError();
                }

            }

            @Override
            public void onError(Response<BaseModel<List<AdvertVideoInfo>>> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }

    private void download(String path, final String filePath, final String filName){

        for (Call call : OkGo.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            Log.e("TAG","已经有任务在执行了runningCalls------" + call.request().tag());
            if(call.request().tag().equals("DOWNLOAD_ADVERT")){
                return;
            }
        }

        OkHttpUtils.getJson(path, "DOWNLOAD_ADVERT", new FileCallback(filePath,filName) {

            @Override
            public void onSuccess(Response<File> response) {
                Log.e("download_advert","success");
                File file = new File(filePath + filName);
                String newFileName = filName +".mp4";
                File newFile = new File(filePath + newFileName);
                if(newFile.exists()){
                    newFile.delete();
                }
                if(file.renameTo(newFile)){
                    iPresenterBase.onDownLoadSuccess(filName);
                    Log.e("download_success","下载的文件信息:"+file.getAbsolutePath());
                }else{
                    iPresenterBase.onError();
                }
            }

            @Override
            public void downloadProgress(Progress progress) {
                Log.e("download_advert",progress+"");
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                iPresenterBase.onError();
            }
        });
    }


}
