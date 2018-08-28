package com.avatech.msfalcos.tools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jventiades on 9/5/2017.
 */

public class Picker {

    private final static String formatLatinDateShort = "dd/MM/yyyy";
    private final static String formatHourShort = "HH:mm";

    public static class Date extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        EditText dateText;
        Boolean blockFutureDate = false;
        Boolean blockPastDate = false;

        public EditText getDateText() {
            return dateText;
        }

        public void setDateText(EditText dateTxt) {
            this.dateText = dateTxt;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

            //trying to hide the F****ing title
            datePickerDialog.setTitle("");
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            if(getBlockFutureDate()) {
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
            if(getBlockPastDate()){
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
            return datePickerDialog;
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);
            if(dateText != null)
                dateText.setText(new SimpleDateFormat(formatLatinDateShort, Locale.US).format(calendar.getTime()));
        }

        public Boolean getBlockFutureDate() {
            return blockFutureDate;
        }

        public void setBlockFutureDate(Boolean blockFutureDate) {
            this.blockFutureDate = blockFutureDate;
        }

        public Boolean getBlockPastDate() {
            return blockPastDate;
        }

        public void setBlockPastDate(Boolean blockPastDate) {
            this.blockPastDate = blockPastDate;
        }
    }



    public static class Time extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        EditText timeText;

        public EditText getTimeText() {
            return timeText;
        }

        public void setTimeText(EditText timeText) {
            this.timeText = timeText;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, true);// DateFormat.is24HourFormat(getActivity())
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if(timeText != null) {
                timeText.setText(new SimpleDateFormat(formatHourShort, Locale.US).format(calendar.getTime()));
            }
        }
    }
}
