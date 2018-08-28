package com.avatech.msfalcos.tools.email;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.avatech.msfalcos.App;
import com.avatech.msfalcos.R;
import com.avatech.msfalcos.model.data.app.DUser;
import com.avatech.msfalcos.model.entity.app.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aortuno on 7/5/2017.
 */

public class SendEmail extends AsyncTask<String, Float, Integer> {

    private String name;
    private String url;
    private Dialog progressDialog;
    private Context activity;
    private String error = "";

    public SendEmail(String name, String url, Context activity) {
        this.name = name;
        this.url = url;
        this.activity = activity;
    }

    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, null,
                activity.getString(R.string.sending));
    }

    @Override
    protected Integer doInBackground(String... params) {
        List<String> emails = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat(App.formatLatinDate);

        emails.add("backups@avatech-bo.com");

        DUser dalUser=new DUser(activity);
        User user = dalUser.get();
        String body="Aplicacion: "+ App.tag+"\n";
        if(user!=null&&user.getId()!=null){
            body+="User: ["+user.getUserCode()+"]"+user.getName()+"\n";
            body+="Inicio de dia: "+dateFormat.format(user.getMobile().getInitialTime())+"\n";
            //body+="Zona: ["+user.getIdZone()+"]"+user.getZoneName()+"\n";
            body+="Imei: "+App.getImei(activity)+"\n";

        }

        body+="Fecha Generacion Base De Datos: "+dateFormat.format(new Date())+"\n";
        body+="\n Dato Adjunto: " + name;

        Email gMail = new Email("avatech.dev@gmail.com", "avatech123", "465", "smtp.gmail.com", emails, "DataBase",body , this.url, name);
        try {
            gMail.createEmailMessage();
            gMail.sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
            error = e.getMessage();
            return 1;
        }
        return 0;
    }

    protected void onProgressUpdate(Float... valores) {

    }

    protected void onPostExecute(Integer bytes) {
        switch (bytes) {
            case 0:
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.message_email_send), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(activity.getApplicationContext(), "Error: "+ error, Toast.LENGTH_SHORT).show();
                break;
        }

        progressDialog.dismiss();
    }
}
