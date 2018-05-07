package com.yht.iptv.utils;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {


    public static void scaleAnimation(View view, int duration, float... value) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleColorAnimation(View view, int duration, float... value) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", value[2],value[3]);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY).with(anim);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleAnimation(View view, int duration, boolean isx, float... value) {
        ObjectAnimator scale;
        if (isx) {
            scale = ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        } else {
            scale = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        }
        scale.setDuration(duration);
        scale.start();
    }

    public static void scaleTranslationAnimation(View view, int duration, float... value) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", value[2], value[3]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleY).with(translationY);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleTranslationXAnimation(View view, int duration, float... value) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", value[2], value[3]);
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(translationX);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleYColorTranslationAnimation(View view,View view2, int duration, int startColor,int endColor,float... value) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", value[2], value[3]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        ObjectAnimator anim = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(),
                startColor, endColor);
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(view2, "translationY", value[2], value[3]*1.5f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleY,translationY,anim,translationY2);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleXYTranslationAnimation(View view, int duration, float... value) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", value[2], value[3]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, translationY);
        set.setDuration(duration);
        set.start();
    }

    public static void scaleXYTranslationXYAnimation(View view, int duration, float... value) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", value[2], value[3]);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", value[4], value[5]);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", value[0], value[1]);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", value[0], value[1]);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, translationX,translationY);
        set.setDuration(duration);
        set.start();
    }


    public static ObjectAnimator rotateAnimation(View view, int repeatCount, long time, float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", start, end);
        animator.setRepeatCount(repeatCount);
        animator.setDuration(time);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    public static ObjectAnimator rotateAnimation(View view, int repeatCount, long time, float pivotX, float pivotY, float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", start, end);
        animator.setRepeatCount(repeatCount);
        animator.setDuration(time);
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        return animator;
    }

    public static void animatorResume(ObjectAnimator animator, long currentPlayTime) {
        if (animator != null) {
            animator.start();
            animator.setCurrentPlayTime(currentPlayTime);
        }
    }

    /**
     * 暂停
     */
    public static long animatorPause(ObjectAnimator animator) {
        long currentPlayTime = 0;
        if (animator != null) {
            currentPlayTime = animator.getCurrentPlayTime();
            animator.cancel();
        }
        return currentPlayTime;
    }



    public static void alphaAnimation(View view, int duration, float... value) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", value[0], value[1]);
        alpha.setDuration(duration);
        alpha.start();
    }


}