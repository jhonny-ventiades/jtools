package com.avatech.msfalcos.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.avatech.msfalcos.App;
import com.avatech.msfalcos.R;
import com.avatech.msfalcos.model.data.app.DUser;
import com.avatech.msfalcos.tools.email.SendEmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jventiades on 23-Nov-17.
 */

public class DataTransfer {

    /**
     * Copy the database from data folder to an user folder
     * @param newName, the name of the database after copy
     * */
    public static String copyDB(String newName, Context context){
        try {


            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                File path = new File(sd, App.pathToExportDB);
                path.mkdirs();

                String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//" + App.databaseName + ".db";
                String backupDBPath = App.pathToExportDB + "/" + newName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return backupDB.getAbsolutePath();
            }else {
                Toast.makeText(context, context.getString(R.string.message_no_backup), Toast.LENGTH_LONG).show();
            }
            return "";
        }catch (IOException e){
            Log.e(App.tag, e.getMessage(), e);
            return "";
        }
    }

    public static void exportDB(String name, boolean sendEmail, Context context, boolean displayMessage){
        try {
            String pathDB = copyDB(name,context);
            if (!pathDB.isEmpty()) {
                if (sendEmail) {
                    SendEmail sendM = new SendEmail(name, pathDB,context);
                    sendM.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    if (displayMessage) {
                        Toast.makeText(context, context.getString(R.string.message_backup_finish), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(App.tag, e.getMessage(),e);
            if (displayMessage) {
                Toast.makeText(context, context.getString(R.string.message_error_backup), Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * Export the DB  and send the database
     * @param sendEmail if is true send the db trought email
     * */
    public static void exportDB(boolean sendEmail, Context context){
        String name = App.databaseName + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()) + ".db";
        exportDB(name,sendEmail,context, false);
    } /**
     * Export the DB  and send the database
     * @param sendEmail if is true send the db trought email
     * @param displayMessage if is true send the db trought email
     * */
    public static void exportDB(boolean sendEmail, Context context, boolean displayMessage){
        String name = App.databaseName + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()) + ".db";
        exportDB(name,sendEmail,context, displayMessage);
    }


    /**
     * Export the list of rows to a csv format
     * @param rows, list of rows to wxport
     * */
    public static Boolean exportToCSV(List<String> rows, String filename) {
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();
            filename += ".csv";

            if (sdCardDir.canWrite()) {
                File path = new File(sdCardDir,App.pathToExportCSV);
                path.mkdirs();
                // the name of the file to export with
                File saveFile = new File(sdCardDir + App.pathToExportCSV, filename);
                FileWriter fw = new FileWriter(saveFile);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String row : rows) {
                    bw.write(row);
                    bw.newLine();
                }
                bw.flush();
            }
        }catch (Exception e){
            Log.e(App.tag, e.getMessage(),e);
            return false;
        }
        return true;
    }

    public static void restoreDB(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//" + App.databaseName + ".db";
            String restoreDBPath = App.pathToExportDB + "/" + App.databaseName + ".db";
            File currentDB = new File(data, currentDBPath);
            File restoreDB = new File(sd, restoreDBPath);
            FileChannel src = new FileInputStream(restoreDB).getChannel();
            FileChannel dst = new FileOutputStream(currentDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Toast.makeText(context, context.getString(R.string.message_restore_finish), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.message_no_restore), Toast.LENGTH_LONG).show();
        }
    }

    public static void restoreDBFrom(String path, Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//" + App.databaseName + ".db";
            //String restoreDBPath = App.pathToExportDB + "/" + App.databaseName + ".db";
            File currentDB = new File(data, currentDBPath);
            File restoreDB = new File(sd, path);
            FileChannel src = new FileInputStream(restoreDB).getChannel();
            FileChannel dst = new FileOutputStream(currentDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Toast.makeText(context, context.getString(R.string.message_restore_finish), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.message_no_restore), Toast.LENGTH_LONG).show();
        }
    }

    public static void chooseDB(Activity activity){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        try {
            activity.startActivityForResult(
                    Intent.createChooser(intent, "Seleccione Base de datos"),
                    123);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(activity, "Instale File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
