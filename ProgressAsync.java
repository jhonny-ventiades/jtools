package com.avatech.msfalcos.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avatech.msfalcos.App;

/**
 * Created by jventiades on 5/16/2018.
 */

public class ProgressAsync<Param,Progress ,Result> extends AsyncTask<Param,Progress,Result> {//

    private ProgressDialog progressDialog;
    private Context context;
    private String message;
    private OnBackground<Result,Param> onBackgroundListener;
    private OnPost<Result> onPostListener;
    private OnProgress<Progress> onProgressListener;
    private OnCancelled onCancelledListener;
    private OnPre onPreListener;
    private boolean hideProgressDialog;

    public ProgressAsync(Context activity) {
        this.context = activity;
    }

    public ProgressAsync(Context activity, String message) {
        this.context = activity;
        this.message = message;
    }
    public ProgressAsync(Context activity, String message, OnBackground<Result,Param> onBackgroundListener) {
        this.context = activity;
        this.message = message;
        this.onBackgroundListener = onBackgroundListener;
    }
    public ProgressAsync(Context activity, String message, OnBackground<Result,Param> onBackgroundListener, OnPost<Result> onPostListener) {
        this.context = activity;
        this.message = message;
        this.onBackgroundListener = onBackgroundListener;
        this.onPostListener = onPostListener;
    }

    public ProgressAsync(Context activity, OnBackground<Result,Param> onBackgroundListener, OnPost<Result> onPostListener) {
        this.context = activity;
        this.onBackgroundListener = onBackgroundListener;
        this.onPostListener = onPostListener;
    }
    @Override
    protected void onPreExecute() {
        if(message == null){
            message = "Cargando";
        }
        if(!hideProgressDialog) {
            progressDialog = ProgressDialog.show(context, null, message);
        }
        if(onPreListener != null){
            onPreListener.execute();
        }
    }

    @SafeVarargs
    @Override
    protected final Result doInBackground(Param... parameters) {
        if(onBackgroundListener != null) {
            return  onBackgroundListener.execute(parameters);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {

        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (onPostListener!=null) {
            onPostListener.execute(result);
        }
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Progress... values) {
        if(onProgressListener != null){
            if (values.length == 0){
                onProgressListener.execute(null);
            } else {
                onProgressListener.execute(values);
            }
        }
    }

    @Override
    protected void onCancelled() {
        if(onCancelledListener != null){
            onCancelledListener.execute();
        }
    }

    /*
     * Callbacks Interfaces to execute the functions
     * */
    public interface OnPre{
        void execute();
    }
    public interface OnBackground<Result,Param>{
        Result execute(Param... params);
    }
    public interface OnPost<Result>{
        void execute(Result result);
    }
    public interface OnProgress<Progress>{
        void execute(Progress... progresses);
    }
    public interface OnCancelled{
        void execute();
    }


    @SafeVarargs
    public final void progress(Progress... type){
        super.publishProgress(type);
    }

    public void progress(){
        super.publishProgress();
    }


    public void setOnBackgroundListener(OnBackground<Result,Param> onBackgroundListener) {
        this.onBackgroundListener = onBackgroundListener;
    }

    public void setOnPostListener(OnPost<Result> onPostListener) {
        this.onPostListener = onPostListener;
    }

    public void setOnProgressListener(OnProgress<Progress> onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public void setOnCancelledListener(OnCancelled onCancelledListener) {
        this.onCancelledListener = onCancelledListener;
    }

    public void setOnPreListener(OnPre onPreListener) {
        this.onPreListener = onPreListener;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHideProgressDialog(boolean hideProgressDialog) {
        this.hideProgressDialog = hideProgressDialog;
    }


}