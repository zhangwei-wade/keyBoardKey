package cn.zhangwei.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

public class KeydemoActivity extends Activity {
    private Context ctx;
    private Activity act;
    private EditText edit;
    private EditText edit1;
    public KeyboardViewCustom keyboardUtil = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        act = this;

        edit = (EditText) this.findViewById(R.id.edit);
        edit1 = (EditText) this.findViewById(R.id.edit1);
        keyboardUtil = new KeyboardViewCustom(act);
        keyboardUtil.addEditText(edit, edit1);
    }

    @Override
    public void onBackPressed() {
//        if (keyboardUtil.isShow()) {
//            keyboardUtil.hideKeyboard();
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (keyboardUtil != null && keyboardUtil.mStartAudio != null)
            keyboardUtil.mStartAudio.release();
    }
}