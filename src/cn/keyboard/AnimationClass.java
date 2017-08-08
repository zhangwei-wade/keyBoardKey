package cn.keyboard;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Administrator on 2017/8/8.
 * <p>
 * 动画旋转移动类
 */
public class AnimationClass {
    private static int duration = 200;

    /**
     * 实现3D旋转
     *
     * @param act  上下文
     * @param view 旋转的view
     */
    public static void startAnimation(Context act, View view) {
        Animation myAnimation_Scale = AnimationUtils.loadAnimation(act, R.anim.translate_animation_in);
        myAnimation_Scale.setDuration(duration);
        view.startAnimation(myAnimation_Scale);
    }

    /**
     * 实现3D旋转
     *
     * @param act  上下文
     * @param view 旋转的view
     */
    public static void endAnimation(Context act, View view) {
        Animation myAnimation_Scale = AnimationUtils.loadAnimation(act, R.anim.translate_animation_out);
        myAnimation_Scale.setDuration(duration);
        view.startAnimation(myAnimation_Scale);
    }

    /**
     * 实现3D旋转
     *
     * @param view         旋转的view
     * @param fromSDegrees 开始旋转角度
     * @param toSDegrees   结束旋转角度
     */
    public static void rotate3D(View view, int fromSDegrees, int toSDegrees, boolean reverse) {
        rotate3D(view, fromSDegrees, toSDegrees, reverse, null);
    }

    /**
     * 实现3D旋转
     *
     * @param view         旋转的view
     * @param fromSDegrees 开始旋转角度
     * @param toSDegrees   结束旋转角度
     * @param listener     动画监听函数
     */
    public static void rotate3D(View view, int fromSDegrees, int toSDegrees, boolean reverse
            , Animation.AnimationListener listener) {
        float centerX = view.getWidth() / 2f;
        float centerY = view.getHeight() / 2f;
        final Rotate3dAnimation rotation = new Rotate3dAnimation(fromSDegrees, toSDegrees, centerX, centerY,
                310.0f, reverse);
        // 动画持续时间300毫秒
        rotation.setDuration(duration);
        // 动画完成后保持完成的状态
        // 设置动画的监听器
        if (listener != null)
            rotation.setAnimationListener(listener);
        view.startAnimation(rotation);
    }


}
