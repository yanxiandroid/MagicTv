package com.yht.iptv.view.music;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yht.iptv.R;
import com.yht.iptv.callback.IPresenterBase;
import com.yht.iptv.model.BaseModel;
import com.yht.iptv.model.MusicDetailBean;
import com.yht.iptv.model.MusicTypeBean;
import com.yht.iptv.presenter.MusicDetailPresenter;
import com.yht.iptv.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MusicTabFragment extends Fragment implements IPresenterBase<List<MusicDetailBean>>, AdapterView.OnItemClickListener, MusicStatusListener {

    private int mPage;//当前的页面
    private Context mContext;
    private List<MusicTypeBean> musicTypeBeanList;//标题类型列表
    private List<MusicDetailBean> musicDetailBeanList;//音乐列表
    private MusicAdapter musicAdapter;//音乐适配器
    private String currentPlayPath;//当前播放音乐地址
    private int playStatus;//当前音乐是否暂停
    private MusicDetailPresenter musicDetailPresenter;


//    public static MusicTabFragment newInstance(int page, List<MusicTypeBean> classList) {
//        Bundle args = new Bundle();
//
//        args.putInt(ARGS_PAGE, page);
//        args.putParcelableArrayList("musicTypeBeanList", (ArrayList<? extends Parcelable>) classList);
//
//        MusicTabFragment fragment = new MusicTabFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("currentPage");
        musicTypeBeanList = getArguments().getParcelableArrayList("musicTypeBeanList");
        currentPlayPath = null;
        Log.e("play_music","onCreate" + mPage);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.music_fragment_detail, container,false);

        ListView musicList = (ListView) view.findViewById(R.id.music_list);

        musicList.setNextFocusUpId(musicTypeBeanList.get(mPage).getId());

        musicDetailBeanList = new ArrayList<>();

        musicAdapter = new MusicAdapter(mContext,musicDetailBeanList);

        musicList.setAdapter(musicAdapter);

        musicList.setOnItemClickListener(this);


        musicList.requestFocus();

        //网络请求
        musicDetailPresenter = new MusicDetailPresenter(mContext,this);
        musicDetailPresenter.request(String.valueOf(musicTypeBeanList.get(mPage).getId()));

        return view;
    }


    private void playMusic(int position){
        //设置播放状态
//        musicAdapter.setOnPlayIconStatus(position,false);
//        musicAdapter.notifyDataSetChanged();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, MusicService.class);
        bundle.putParcelableArrayList("musicList", (ArrayList<? extends Parcelable>) musicDetailBeanList);
        bundle.putInt("type", Constants.AUDIO_LIST);
        bundle.putInt("position",position);
        bundle.putInt("mPage",mPage);
        intent.putExtras(bundle);
        mContext.startService(intent);
    }


    @Override
    public void onSuccess(BaseModel<List<MusicDetailBean>> dataList) {
        if(dataList.data != null && dataList.data.size() > 0){
            if(dataList.data.get(0) != null){
                List<MusicDetailBean> data = dataList.data;
                musicDetailBeanList.clear();
                musicDetailBeanList.addAll(data);
                musicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //注册监听服务通知的接口
        MusicStatusManger.getInstance().setMusicStatusListener(this);
        playMusic(position);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if(isVisibleToUser){
            Log.e("play_music","music-visible" + mPage);
            if(currentPlayPath != null) {
                //更新按钮状态
                for (int i = 0 ; i < musicDetailBeanList.size(); i++) {
                    if (musicDetailBeanList.get(i).getFileUpload().getPath().equals(currentPlayPath)) {
                        musicAdapter.setOnPlayIconStatus(i,playStatus);
                        musicAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                musicAdapter.setOnPlayIconStatus(-1,1);
                musicAdapter.notifyDataSetChanged();
            }
        if(musicAdapter!= null)
            if(musicAdapter.getCount() == 0){
                musicDetailPresenter.request(String.valueOf(musicTypeBeanList.get(mPage).getId()));
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MusicTabFragment","MusicTabFragment--" + mPage);
        if(MusicStatusManger.getInstance() != null) {
            MusicStatusManger.getInstance().releaseRes();
        }
    }

    //服务状态监听
    @Override
    public void onStatus(String path,int position,int playStatus) {
        //获取FragmentManager管理fragment.设置全局fragment一样的值
        FragmentManager fragmentManager = ((MusicActivity) mContext).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment instanceof MusicTabFragment) {
                //设置值
                ((MusicTabFragment) fragment).currentPlayPath = path;
                ((MusicTabFragment) fragment).playStatus = playStatus;
            }
        }

        //音乐暂停状态
        //设置播放状态
        musicAdapter.setOnPlayIconStatus(position,playStatus);
        musicAdapter.notifyDataSetChanged();

        if(playStatus == 2){
//            ToastUtils.showToast(mContext, R.string.error_music);
        }
    }
}
