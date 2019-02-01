package com.avatech.msfkralv2.tools;


import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.avatech.msfkralv2.R;

public final class EasyAlertDialog {


    public static void show(Context context, int message) {
        EasyAlertDialog.show(context,context.getString(message),null,null,null,null);
    }

    public static void show(Context context, String message) {
        EasyAlertDialog.show(context, message,null,null,null,null);
    }

    public static void show(Context context, int message, int label, final DialogInterface.OnClickListener listener) {
        EasyAlertDialog.show(context,context.getString(message),context.getString(label),listener, null, null);
    }
    public static void show(Context context, int message, int label, final DialogInterface.OnClickListener listener, int negativeLabel, final DialogInterface.OnClickListener  negativeListener) {
        EasyAlertDialog.show(context,context.getString(message),context.getString(label),listener, context.getString(negativeLabel), negativeListener);
    }

    public static void show(Context context, String message, String positiveLabel, final DialogInterface.OnClickListener positiveListener, String negativeLabel, final DialogInterface.OnClickListener  negativeListener){
        AlertDialog alertDialog = null;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        if (positiveLabel != null && positiveListener != null) {
            alert.setPositiveButton(positiveLabel, positiveListener::onClick);
        }
        if (negativeLabel != null && negativeListener != null) {
            alert.setNegativeButton(negativeLabel, negativeListener::onClick);
        }
        alertDialog = alert.create();
        alertDialog.show();
    }

   
}
