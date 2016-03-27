package com.example.jarryd.assignment_1;

import android.renderscript.ScriptGroup;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jarryd on 28/03/16.
 */
public interface TextViewListenerHelper {

    void setListenersForEditText(EditText editText);

    void setOnClickListenerForEditText(EditText editText);

    void setOnChangeFocusListenerForEditText(EditText editText);

    void setOnEditorActionListenerForEditText(TextView editText);
}
