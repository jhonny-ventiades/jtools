package com.avatech.msfalcos.tools;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jventiades on 3/14/2018.
 */

public class DateText extends android.support.v7.widget.AppCompatEditText {

    private final static String formatLatinDateShort = "dd/MM/yyyy";

    Calendar calendar = Calendar.getInstance();
    Picker.Date mDatePicker;
    Context context;
    public DateText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DateText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        //mDatePicker.show(getFragmentManager(), "Select date");
    }

    private void init(){
        setText(new SimpleDateFormat(formatLatinDateShort, Locale.US).format(calendar.getTime()));
        mDatePicker = new Picker.Date();
        mDatePicker.setDateText(this);
        mDatePicker.setBlockFutureDate(true);
    }
}
