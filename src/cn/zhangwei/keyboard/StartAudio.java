package cn.zhangwei.keyboard;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Administrator on 2017/8/7.
 * 播放声音文件
 */
public class StartAudio {

    private final Activity act;
    private SoundPool soundPool;
    private float volume;
    private int loadId;
    private AudioManager mgr;

    public StartAudio(Activity act) {
        this.act = act;
        mgr = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = streamVolumeCurrent / streamVolumeMax;
    }

    public void start(int id) {
        if (soundPool == null) {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
            loadId = soundPool.load(act, id, 1);
        }
    }

    public void play() {
        soundPool.play(loadId, volume, volume, 1, 0, 1f);
    }

    public void release() {
        if (soundPool != null)
            soundPool.release();
        soundPool = null;
    }
}
