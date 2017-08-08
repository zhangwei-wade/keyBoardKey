package cn.zhangwei.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
//import com.dynamicode.sftProject.Interface.OnClickListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class KeyboardViewCustom {
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    public boolean isnun = true;// 是否数据键盘
    public boolean isupper = false;// 是否大写
//    public OnClickListener onClickListener;

    private EditText mEdit;
    private String TAG = "KeyboardViewCustom";
    public StartAudio mStartAudio;

    public KeyboardViewCustom(Activity act) {
        this.act = act;
        k1 = new Keyboard(act, R.xml.qwerty);
        k2 = new Keyboard(act, R.xml.symbols);
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k2);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
        onTouch(act);
        mStartAudio = new StartAudio(act);
        mStartAudio.start(R.raw.dtmf8);
    }
//
//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    /**
     * 设置editText之外的地方自定义键盘消失
     */
    private void onTouch(Activity act) {
        act.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof EditText)) {
                    if (isShow()) {
                        hideKeyboard();
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public void setEditText(EditText edit) {
        this.mEdit = edit;
        hideSoftInputMethod();
        if (!isShow()) {
            showKeyboard();
        }
        edit.setOnKeyListener(okKey);
    }

    /**
     * 监听点击事件判断键盘如果没有显示就显示
     */
    private View.OnTouchListener onTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isShow()) {
                showKeyboard();
            }
            return false;
        }
    };
    /**
     * 监听返回按键如果键盘显示先隐藏键盘
     */
    private View.OnKeyListener okKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && isShow()) {
                hideKeyboard();
                return true;
            }
            return false;
        }
    };

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
            mStartAudio.play();
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEdit.getText();
            int start = mEdit.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
                keyboardView.setVisibility(View.INVISIBLE);
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                keyboardView.setKeyboard(k1);
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isnun) {
                    AnimationClass.rotate3D(keyboardView, 0, 90, true, new AnimationListener(270, 360));
                } else {
                    AnimationClass.rotate3D(keyboardView, 360, 270, true, new AnimationListener(90, 0));
                }
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    mEdit.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < mEdit.length()) {
                    mEdit.setSelection(start + 1);
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keylist = k1.getKeys();
        if (isupper) {//大写切换小写
            isupper = false;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//小写切换大写
            isupper = true;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
            AnimationClass.startAnimation(act, keyboardView);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            AnimationClass.endAnimation(act, keyboardView);
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isShow() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 禁掉系统软键盘
     */
    public void hideSoftInputMethod() {
        act.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            mEdit.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mEdit, false);
            } catch (NoSuchMethodException e) {
                mEdit.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void addEditText(EditText... eds) {
        for (int i = 0; i < eds.length; i++) {
            EditText editText = eds[i];
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        setEditText(editText);
                }
            });
            editText.setOnTouchListener(onTouch);
        }
    }

    /**
     * 注册在ListView点击动画中的动画监听器，用于完成ListView的后续动画。
     *
     * @author guolin
     */
    class AnimationListener implements Animation.AnimationListener {
        int fromEDegrees;
        int toEDegrees;

        public AnimationListener(int fromEDegrees, int toEDegrees) {
            this.fromEDegrees = fromEDegrees;
            this.toEDegrees = toEDegrees;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 当ListView的动画完成后，还需要再启动ImageView的动画，让ImageView从不可见变为可见
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            if (isnun) {
                isnun = false;
                keyboardView.setKeyboard(k1);
            } else {
                isnun = true;
                keyboardView.setKeyboard(k2);
            }
            // 构建3D旋转动画对象，旋转角度为270到360度，这使得ImageView将会从不可见变为可见
            AnimationClass.rotate3D(keyboardView, fromEDegrees, toEDegrees, false);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

}
