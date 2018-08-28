package com.avatech.msfalcos.tools;


import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public final class EasySnackBar {

    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_LONG = Snackbar.LENGTH_LONG;
    public static final int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;



    public static void show(Activity activity, int message, int duration) {
        EasySnackBar.show(activity,activity.getString(message),duration,null,null);
    }

    public static void show(Activity activity, String message, int duration) {
        EasySnackBar.show(activity, message,duration,null,null);
    }

    public static void show(Activity activity, int message, int duration, int label, final View.OnClickListener listener) {
        EasySnackBar.show(activity,activity.getString(message),duration,activity.getString(label),listener);
    }

    public static void show(Activity activity, String message, int duration, String actionLabel, final View.OnClickListener listener){
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup)
                activity.findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(viewGroup, message, duration);

        if(actionLabel!=null){
            snackbar.setAction(actionLabel, listener);
        }
        hideKeyboard(activity);
        snackbar.show();
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
