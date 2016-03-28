package com.example.jarryd.assignment_1;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jarryd on 28/03/16.
 */

public class TextViewListenerHelperImpl implements TextViewListenerHelper {
    public InputMethodManager inputMethodManager;

    public TextViewListenerHelperImpl(InputMethodManager inputMethodManager) {
        this.inputMethodManager = inputMethodManager;
    }


    @Override
    public void setListenersForEditText(EditText editText) {
        setOnClickListenerForEditText(editText);
        setOnChangeFocusListenerForEditText(editText);
        setOnEditorActionListenerForEditText(editText);
    }

    @Override
    public void setOnClickListenerForEditText(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View editText) {
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    @Override
    public void setOnChangeFocusListenerForEditText(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View editText, boolean hasFocus) {
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    @Override
    public void setOnEditorActionListenerForEditText(TextView editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }
}
