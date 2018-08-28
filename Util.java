package com.avatech.msfalcos.tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;


import com.avatech.msfalcos.App;
import com.avatech.msfalcos.R;
import com.avatech.msfalcos.activities.order.OrderActivity;
import com.avatech.msfalcos.model.data.app.DMobile;
import com.avatech.msfalcos.model.data.app.DUser;
import com.avatech.msfalcos.model.data.base.DConfigParameter;
import com.avatech.msfalcos.model.entity.Enumerators;
import com.avatech.msfalcos.model.entity.app.Mobile;
import com.avatech.msfalcos.model.entity.app.User;
import com.avatech.msfalcos.model.entity.base.ConfigParameter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by jhonny.ventiades on 30/11/2017.
 */
public class Util {

    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        return networkInfo == null ? false : networkInfo.isConnected();
    }

    public static boolean isConnectedToMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
        return networkInfo == null ? false : networkInfo.isConnected();
    }

    public static boolean isConnected(Context context) {
        if (isConnectedToWifi(context)) {
            return true;
        } else {
            return isConnectedToMobile(context);
        }
    }

    /**
     * Add padding to the right
     * @param s is the string which need the padding
     * @param n amount of padding to add
     * @return the string with the padding
     * */
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    /**
     * Add padding to the Left
     * @param s is the string which need the padding
     * @param n amount of padding to add
     * @return the string with the padding
     * */

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    /**
     * Add padding to the Center
     * @param s is the string for its long divided 2 more
     * @param n divided 2
     * @return the string with the padding calculating the left and the right
     * */

    public static String padCenter(String s, int n) {
        return padRight(padLeft(s, ((n / 2) + (s.length() / 2))), n);
    }
    /**
     * Convierte Numeros en literales
     *
     * @param fltNumber the amount that we enter converts it to Integer, then we ask if it is greater than or equal
     *                  to the quantities that we have, that is to say, we compare in what rank it is of millions or hundreds.
     *                  Following this, a division will be made in the range in which the number we entered is found.
     * @return  Finally the number is converted to a String and we return it.
     */
    private static String NumberToLiteral(double fltNumber) {
        StringBuilder strLiteral = new StringBuilder();
        String[] strUnidades = {"un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez", "once", "doce", "trece", "catorce", "quince"};
        String[] strDecenas = {"dieci", "veint", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
        String[] strCentenas = {"ciento ", "doscientos ", "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ", "setecientos ", "ochocientos ", "novecientos "};
        String[] strMiles = {" mil ", " millones "};
        int intMontoentero = (int) fltNumber;
        long lngParcial = 0;
        String strComodin = "";
        while (intMontoentero > 0) {
            if (intMontoentero >= 1000000) {
                lngParcial = (long) (intMontoentero / 1000000);
                intMontoentero = intMontoentero % 1000000;
                if (lngParcial == 1) {
                    strLiteral.append("un millon");
                } else {
                    strLiteral.append(NumberToLiteral(lngParcial) + strMiles[1]);
                }
            } else if (intMontoentero >= 1000) {
                lngParcial = (long) (intMontoentero / 1000);
                intMontoentero = intMontoentero % 1000;
                if (lngParcial == 1) {
                    strLiteral.append("un " + strMiles[0]);
                } else {
                    strLiteral.append(NumberToLiteral(lngParcial) + strMiles[0]);
                }
            } else if (intMontoentero >= 100) {
                lngParcial = (long) (intMontoentero / 100);
                intMontoentero = intMontoentero % 100;
                if ((lngParcial == 1) && (intMontoentero == 0)) {
                    strLiteral.append("cien");
                } else {
                    strLiteral.append(strCentenas[(int) (lngParcial - 1)]);
                }
            } else if (intMontoentero > 15) {
                if ((intMontoentero >= 20) && (intMontoentero < 30)) {
                    strComodin = (intMontoentero % 10 == 0 ? "e" : "i");
                } else if ((intMontoentero % 10 != 0) && (intMontoentero > 30)) {
                    strComodin = " y ";
                } else {
                    strComodin = "";
                }
                lngParcial = (long) (intMontoentero / 10);
                intMontoentero = intMontoentero % 10;
                strLiteral.append(strDecenas[(int) (lngParcial - 1)] + strComodin);
            } else {
                strLiteral.append(strUnidades[(int) (intMontoentero - 1)]);
                intMontoentero = 0;
            }
        }
        return strLiteral.toString();
    }
    /**
     * Add Round
     * @param Rval is a double that is used to calculate a round number
     * @param Rpl It's the number that helps calculate
     * @return the double with the result
     *
     *
     * */
    public static double Round(double Rval, int Rpl) {
        double p = (double) Math.pow(10, Rpl);
        Rval = Rval * p;
        double tmp = Math.round(Rval);
        return (double) tmp / p;
    }
    /**
     * Add GetDecimal
     * @param str is a string that is used to check if it is empty.
     *            if this is true it will be replaced with a value
     *            followed by this I will return it
     * @return the double with the result
     * */
    public static int getDecimal(String str) {
        int value = 0;
        if (str != null && !str.isEmpty()) {
            str = str.replace(",", ".");
            if (str.indexOf(".") > 0) {
                str = str.substring(str.indexOf(".") + 1);
            } else {
                str = "0";
            }
            value = Integer.parseInt(str);
        }

        return value;
    }
    /**
     * Add FormatDouble
     * @param dbl is a double we ask if it is less than one,
     *            if so, we use the auxiliary variable to calculate a random number.
     *            and this will be our result.
     * @return the String with the result that has the random number.
     * */
    public static String formatDouble(double dbl) {
        String doubleString = "";
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.00", dfs);
        doubleString = format.format(Math.round(dbl * 100) / 100d);
        if (dbl < 1) {
            doubleString = "0" + doubleString;
        }
        return doubleString;
    }

    public static String formatDoubleHundred(double dbl) {
        String doubleString = "";
        DecimalFormat format = new DecimalFormat("###,###.00");
        doubleString = format.format(Math.round(dbl * 100) / 100d);
        if (dbl < 1) {
            doubleString = "0" + doubleString;
        }
        return doubleString;
    }

    /*public static String AmountToLiteral(double fltNumber) {
        String strMontoLiteral = "";
        double fltMontoFraccion = 0d;
        if (fltNumber > 0) {
            if (fltNumber > 1) {
                strMontoLiteral = NumberToLiteral(fltNumber);
                fltMontoFraccion = Round(fltNumber - (long) (fltNumber), 2);
                if (fltMontoFraccion > 0d) {
                    fltMontoFraccion = (double) ((long) (fltMontoFraccion * 100));
                    strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                    strMontoLiteral += " con " + Long.toString((long) fltMontoFraccion) + "/100 ";
                } else {
                    if (!(strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()).equals("0"))) {
                        strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                        strMontoLiteral += " con 00/100 ";
                    } else {
                        strMontoLiteral = "0";
                    }
                }
            } else {
                strMontoLiteral = Integer.toString((int) (fltNumber * 100)) + "/100 Centavos";
            }
        }
        return strMontoLiteral;
    }*/


   /* public static String AmountToLiteral(double fltNumber) {
        String strMontoLiteral = "";
        BigDecimal fltMontoFraccion ;

        if (fltNumber > 0) {
            BigDecimal bd = new BigDecimal(String.valueOf(fltNumber));
            if (fltNumber > 1) {
                strMontoLiteral = NumberToLiteral(fltNumber);
                fltMontoFraccion = bd.remainder( BigDecimal.ONE );
                if (fltMontoFraccion.doubleValue() > 0d) {
                    fltMontoFraccion =bd.subtract(bd.setScale(0, RoundingMode.FLOOR)).movePointRight(bd.scale());
                    strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                    strMontoLiteral += " con " +Util.formatDoubleLiteral( fltMontoFraccion.intValueExact()) + "/100 Bolivianos";
                } else {
                    if (!(strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()).equals("0"))) {
                        strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                        strMontoLiteral += " con 00/100 Bolivianos";
                    } else {
                        strMontoLiteral = "0";
                    }
                }
            } else {
                fltMontoFraccion =bd.subtract(bd.setScale(0, RoundingMode.FLOOR)).movePointRight(bd.scale());
                strMontoLiteral = Util.formatDoubleLiteral(fltMontoFraccion.intValueExact()) + "/100 Bolivianos";
            }
        }
        return strMontoLiteral;
    }*/
    /**
     * Add FormatDouble
     * @param fltNumber returns the double number in a string literal number
     * @return the String with the result.
     * */

    public static String AmountToLiteral(double fltNumber) {
        String strMontoLiteral = "";
        BigDecimal fltMontoFraccion ;

        if (fltNumber > 0) {
            BigDecimal bd = new BigDecimal(String.valueOf(fltNumber));

            if (fltNumber > 1) {
                strMontoLiteral = NumberToLiteral(fltNumber);
                fltMontoFraccion = bd.remainder( BigDecimal.ONE );
                if (fltMontoFraccion.doubleValue() > 0d) {
                    fltMontoFraccion=fltMontoFraccion.multiply(new BigDecimal(100) );
                    strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                    strMontoLiteral += " con " +Util.formatDoubleLiteral( fltMontoFraccion.intValue()) + "/100 Bolivianos";
                } else {
                    if (!(strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()).equals("0"))) {
                        strMontoLiteral = strMontoLiteral.substring(0, 1).toUpperCase(Locale.getDefault()) + strMontoLiteral.substring(1);
                        strMontoLiteral += " con 00/100 Bolivianos";
                    } else {
                        strMontoLiteral = "0";
                    }
                }
            } else {
                fltMontoFraccion =bd.remainder( BigDecimal.ONE );
                strMontoLiteral = Util.formatDoubleLiteral(fltMontoFraccion.multiply(new BigDecimal(100)).intValue()) + "/100 Bolivianos";
            }
        }
        return strMontoLiteral;
    }
    /**
     * Add FormatDouble
     * @param dbl is the number that we enter.
     *            This class represents the set of symbols (such as the decimal separator, the grouping separator, etc.)
     *            necessary for DecimalFormat to format numbers
     * @return the int with the result.
     * */
    public static String formatDoubleLiteral(int dbl) {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("00.##", dfs);
        return format.format(dbl);
    }
    /**
     * Add FormatDouble
     * @param datePicker it gets the day the month of the year and returns it
     * @return on a calendar.
     * */
    public static Date getDateFromDatePicket(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    /**
     * Add FormatDouble
     * @param str if the value is empty or null,
     *            replace the value with a "." or ","
     *            once replaced try to convert the string that reaches Decimal
     * @return value whit the Decimal.
     * */
    public static double getValue(String str) {
        double value = 0;
        if (str != null && !str.isEmpty()) {
            str = str.replace(",", ".");
            value = Double.parseDouble(str);
        }
        return value;
    }

    public static String generateCancellationCode() {
        String Letras = "abcdefghijklmnopqrstuvwxyz0123456789";
        String strCode = "";
        int intIndex = 0;
        Random random = new Random();

        for (int intContador = 0; intContador <= 4; intContador++) {
            intIndex = (int) ((int) Math.floor((double) ((random.nextInt(36)) + 1)));
            strCode += String.valueOf(Letras.charAt(intIndex - 1));
        }
        return strCode;
    }



    public static boolean hasDecimal(Double number) {
        if (number % 1 > 0)
            return true;
        return false;
    }
    /**
     * Add Round
     * @param dblnToR is a double that is used to calculate a round number
     * @param intCntDec It's the number that helps calculate
     * @return the double with the result
     * */
    public static Double Redondear(double dblnToR, int intCntDec) {
        double dblPot = 0;
        double dblF = 0;
        if (dblnToR < 0)
            dblF = -0.5;
        else
            dblF = 0.5;
        dblPot = Math.pow(10, intCntDec);
        Double r = (Double) ((int) (dblnToR * dblPot * (1 + 0.0000000000000001) + dblF) / dblPot);
        return r;
    }

    public static Bitmap encodeQRAsBitmap(String contents,
                                          BarcodeFormat format,
                                          int desiredWidth,
                                          int desiredHeight) throws WriterException {
        Hashtable<EncodeHintType,Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new Hashtable<EncodeHintType,Object>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }




    public static String addZeros(String value){
        String code = "";
        try {
            code = String.format("%1$5s", value.trim()).replaceAll(" ", "0");
        } catch (Exception e) {
            code = value;
        }
        return code;
    }

    public static String toCamelCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String subString(String s,int endIndex){
        return s.substring(0, ((s.length()<endIndex || endIndex<0 )?s.length():endIndex));

    }

    public static String formatDoubleQr(double dbl) {
        DecimalFormatSymbols dfs=DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.##",dfs);
        return format.format(dbl);
    }

    /**
     * Add Round
     * @param date gets the hour the minute seconds and milliseconds
     * @return the time.
     * */
    public static boolean isToday(Date date){
        if(date ==null)
            return false;
        //validate if is an old payment
        Calendar c = Calendar.getInstance();
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // and get that as a Date
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        return c1.getTime().equals(c.getTime());
    }

    public static void showAlert(Context context, String title, String message,Callable positiveButton, Callable negativeButton){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.cancel),(dialog,id)->{
                    dialog.cancel();
                    try {
                        positiveButton.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setPositiveButton(context.getString(R.string.ok),(dialog,id)->{
                    dialog.dismiss();
                    try {
                        negativeButton.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showSimpleAlert(Context context, String title, String message){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    //todo add to unit test
    public static  Double unConvertEquivalence(int equivalence, Double quantity){//not check equivalence on order
        try {
            int whole = ((int) (quantity / equivalence));
            Double decimal = (quantity % equivalence);

            Double factorial = decimal / 100;
            return (whole + factorial);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    //todo add to unit test
    public static Double convertEquivalence(int equivalence, Double quantityTemp){//not check equivalence on order
        try {
            //int decimal = (int)(quantity%1)*10;
            //double decimal = quantity - Math.floor(quantity);
            String q = quantityTemp.toString();
            String[] parts = q.split("\\.");
            String dq = parts[1];
            if(dq.length() == 1) dq+="0";
            int fraction = Integer.parseInt(dq);
            int decimal = Integer.parseInt(parts[0]);
            if (fraction == 0) {
                return Double.valueOf(decimal* equivalence);
            }
            if (fraction < equivalence) {
                Double result = 0.0;
                result += decimal*equivalence;
                result += fraction;
                return result;
            }
            return 0.0;
        }catch (Exception e){
            e.printStackTrace();
            return 0.0;
        }
    }

    public static boolean isNumeric(String value){
        try{
            double d = Double.parseDouble(value);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static String getParameter(Context context, int idParameter) {
        return  getParameter(context, (long)idParameter);
    }

    public static String getParameter(Context context, Long idParameter){
        try {
            return new DConfigParameter(context).get(idParameter).getValue();
        } catch (Exception e){
            LogWriter.e(e.getMessage(),e);
            return "";
        }
    }

    public static boolean validateMobileDate(Context context){
        User user = new DUser(context).get();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(App.formatDateShort);
        if(dateFormat.format(date).equals(dateFormat.format(user.getProcessDate()))) {
            return true;
        } else {
            if(App.getInstance().devMode){
                Toast.makeText(context, "DEV MODE! Sin restricción por fecha de proceso", Toast.LENGTH_LONG).show();
                return true;
            }
            new AlertDialog.Builder(context).setTitle(null)
                    .setMessage(context.getString(R.string.message_date_different_as_mobile,dateFormat.format(date),dateFormat.format(user.getProcessDate())))
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
            return false;
        }
    }
}
