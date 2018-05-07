package com.yht.iptv.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.yht.iptv.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by admin on 2017/10/10.
 */

public class ShowImageUtils {

    /*private static RequestOptions options = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true);

    private static RequestOptions options2 = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .priority(Priority.HIGH);
    private static RequestOptions options3 = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .priority(Priority.HIGH);*/

    /**
     * 动画加载
     *
     * @param context
     * @param url
     */
    public static void showImageView(Context context, String url, ImageView imageView) {
        /*Glide.with(context)
                .load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(imageView);*/
        /*GlideApp.with(context)
                .load(url)
                .centerCrop()
                .thumbnail(GlideApp.with(context).load(url))
                .transition(new DrawableTransitionOptions().crossFade(300))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);*/
        Glide.with(context)
                .load(url)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .thumbnail(0.2f)
                .crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 动画加载
     *
     * @param url
     */
    public static void showImageView(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .thumbnail(0.2f)
                .crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 动画加载
     *
     * @param context
     * @param url
     */
    public static void showImageViewNoMemory(Context context, String url, ImageView imageView,int duration) {
        Glide.with(context)
                .load(url)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .crossFade(duration)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

    /**
     * 动画加载
     *
     * @param context
     * @param url
     */
    public static void showImageViewNoMemory(Fragment context, String url, ImageView imageView,int duration) {
        Glide.with(context)
                .load(url)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .crossFade(duration)
                .thumbnail(0.2f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 动画加载
     *
     * @param context
     */
    public static void showImageViewNoMemory(Context context, int resource, ImageView imageView,int duration) {
        Glide.with(context)
                .load(resource)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .crossFade(duration)
                .thumbnail(0.2f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 动画加载
     *
     * @param context
     */
    public static void showImageViewNoMemory(Fragment context, int resource, ImageView imageView,int duration) {
        Glide.with(context)
                .load(resource)
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .crossFade(duration)
                .thumbnail(0.2f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }



    /**
     * 非动画加载
     *
     * @param context
     * @param url
     */
    public static void showImageViewGone(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

    /**
     * 非动画加载
     *
     * @param context  生命周期为Fragment
     * @param url
     */
    public static void showImageViewGone(Fragment context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为Activity
     * @param resource 资源文件
     * @param imageView View
     */
    public static void showImageView(final Context context, int resource, final ImageView imageView) {
        Glide.with(context)
                .load(resource)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为FRAGMENT
     * @param resource 资源文件
     * @param imageView View
     */
    public static void showImageView(final Fragment context, int resource, final ImageView imageView) {
        Glide.with(context)
                .load(resource)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为Activity
     * @param url 资源文件
     * @param imageView View
     */
    public static void showImageViewTrans(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .skipMemoryCache(true)
                .crossFade(2000)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为Activity
     * @param url 资源文件
     * @param imageView View
     */
    public static void showImageViewTrans(final Context context, int url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .skipMemoryCache(true)
                .crossFade(2000)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为FRAGMENT
     * @param url 资源文件
     * @param imageView View
     */
    public static void showImageViewTrans(final Fragment fragment, String url, final ImageView imageView,final Context context) {
        Glide.with(fragment)
                .load(url)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .skipMemoryCache(true)
                .crossFade(2000)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为Activity
     * @param resource 资源文件
     * @param imageView View
     */
    public static void showImageViewGone(final Context context, int resource, final ImageView imageView) {
        Glide.with(context)
                .load(resource)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     *
     * @param context 生命周期为FRAGMENT
     * @param resource 资源文件
     * @param imageView View
     */
    public static void showImageViewGone(final Fragment context, int resource, final ImageView imageView) {
        Glide.with(context)
                .load(resource)
                .dontAnimate()
                .centerCrop()
//                .placeholder(R.drawable.photo_loading_failed)
                .error(R.drawable.photo_failed)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

//    /**
//     *
//     * @param context 生命周期为Activity
//     * @param resource 资源文件
//     * @param bgLayout View
//     */
//    public static void showImageView(final Context context, int resource, final View bgLayout) {
//        GlideApp.with(context).load(resource)// 设置错误图片
//                .into(new SimpleTarget<Drawable>() {
//
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        if (bgLayout instanceof ImageView) {
//                            ((ImageView) bgLayout).setImageDrawable(resource);
//                        } else {
//                            bgLayout.setBackground(resource);
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        if (bgLayout instanceof ImageView) {
//                            ((ImageView) bgLayout).setImageDrawable(errorDrawable);
//                        } else {
//                            bgLayout.setBackground(errorDrawable);
//                        }
//                    }
//                });
//    }

//    /**
//     *
//     * @param context 生命周期为fragment
//     * @param resource 资源文件
//     * @param bgLayout View
//     */
//    public static void showImageView(final Fragment context, int resource, final View bgLayout) {
//        GlideApp.with(context).load(resource)// 设置错误图片
//                .into(new SimpleTarget<Drawable>() {
//
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        if (bgLayout instanceof ImageView) {
//                            ((ImageView) bgLayout).setImageDrawable(resource);
//                        } else {
//                            bgLayout.setBackground(resource);
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        if (bgLayout instanceof ImageView) {
//                            ((ImageView) bgLayout).setImageDrawable(errorDrawable);
//                        } else {
//                            bgLayout.setBackground(errorDrawable);
//                        }
//                    }
//                });
//    }
}
