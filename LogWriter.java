package com.avatech.msfalcos.tools;

import android.os.Environment;
import android.util.Log;

import com.avatech.msfalcos.App;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Date;

/**
 * Created by jventiades on 1/2/2018.
 */

public class LogWriter {
    private static File pathDir;
    private static String fileName;
    private static File file;

    static {
        try {
            pathDir = new File(Environment.getExternalStorageDirectory(), "/avatech/logs");
            //validamos la creacion de un archivo
            fileName = "log_" + new SimpleDateFormat(App.formatDateLog).format(new Date()) + ".txt";
            file = new File(pathDir, fileName);
            pathDir.mkdirs();
            if (!(file.exists())) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String type, String message) {
        FileWriter logWriter = null;
        BufferedWriter out = null;
        try {
            logWriter = new FileWriter(file, true);
            out = new BufferedWriter(logWriter);
            message = "[" + new SimpleDateFormat(App.formatTime).format(new Date()) + "] " + type + " " + message + "\n";
            out.write(message);
            out.close();
            logWriter.close();
            //Log.d(App.tag, message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (logWriter != null) {
                try {
                    logWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void i(String message) {
        write("INFO", message);
        Log.i(App.tag,message);
    }

    public static void d(String message) {
        write("DEBUG", message);
        Log.d(App.tag,message);
    }

    public static void w(String message) {
        write("WARNING", message);
        Log.w(App.tag,message);
    }

    public static void e(String message) {
        write("ERROR", message);
        Log.e(App.tag,message);
    }
    public static void e(String message, Exception e) {
        write("ERROR", message);
        Log.e(App.tag,message,e);
    }

    public static File getPathDir() {
        return pathDir;
    }

    public static String getFileName() {
        return fileName;
    }


}
