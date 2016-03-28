package com.example.jarryd.assignment_1;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jarryd on 28/03/16.
 * <p/>
 * Custom interface to set the various Listeners for certain events affecting EditText Views
 * (avoids code duplication)
 */
public interface TextViewListenerHelper {

    void setListenersForEditText(EditText editText);

    void setOnClickListenerForEditText(EditText editText);

    void setOnChangeFocusListenerForEditText(EditText editText);

    void setOnEditorActionListenerForEditText(TextView editText);
}
