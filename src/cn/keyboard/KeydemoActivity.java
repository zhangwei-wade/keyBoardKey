package cn.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class KeydemoActivity extends Activity {
    private Context ctx;
    private Activity act;
    private EditText edit;
    private EditText edit1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        act = this;

        edit = (EditText) this.findViewById(R.id.edit);
        edit.setInputType(InputType.TYPE_NULL);

        edit1 = (EditText) this.findViewById(R.id.edit1);
        KeyboardViewCustom view = new KeyboardViewCustom(this);
        view.addEditText(edit, edit1);
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
    }
}