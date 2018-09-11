package com.aidisa.app.tools;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import com.aidisa.app.R;

public class FieldValidator {
    private static final String INCORRECT_VALUES = "Valor incorrecto";
    private static final String VALUE_SHORT = "Se requiere minimo de %s caracteres";

    private static final String PASSWORD_INCORRECT = "Contraseña incorrecta";
    private static final String PASSWORD_WRONG_LENGTH= "Contraseña debe ser mas de 3 caract. ";
    private static final String PASSWORD_ARE_NOT_SAME= "Contraseña son diferentes";
    private static final String EMAIL_INCORRECT= "Correco electrónico incorrecto";

    public static boolean areCorrectFields(EditText... params){
        return FieldValidator.areCorrectFields(false,0, params);
    }

    public static boolean areCorrectFields(int minChar, EditText... params){
        return FieldValidator.areCorrectFields(true, minChar, params);
    }

    private static boolean areCorrectFields(boolean validateMinChars, int minChar, EditText... params){
        boolean correctFields = true;
        for (EditText param : params) {
            if(param.getText().toString().trim().isEmpty()) {
                param.requestFocus();
                param.setError(INCORRECT_VALUES);
                correctFields = false;
            }
            if (validateMinChars){
                if (param.getText().toString().trim().length() < minChar){
                    param.requestFocus();
                    param.setError(String.format(VALUE_SHORT,minChar));
                    correctFields = false;
                }
            }

            if (param.getInputType()== InputType.TYPE_NUMBER_VARIATION_PASSWORD
                    || param.getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD
                    || param.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                if(!FieldValidator.isCorrectPassword(param)){
                    correctFields = false;
                }
            }

            if (param.getInputType()== InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    || param.getInputType()== InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT
                    || param.getInputType()== InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS) {
                if(!FieldValidator.isValidEmail(param)){
                    correctFields = false;
                }
            }
        }
        return correctFields;
    }

    public static boolean areSamePasswords(EditText param1, EditText param2){
        if (areCorrectFields(param1,param2)
                && isCorrectPassword(param1)
                && isCorrectPassword(param2)
                && (param1.getText().toString().trim()
                        .equals(param2.getText().toString().trim()))){
            return true;
        }
        param1.requestFocus();
        param1.setError(PASSWORD_ARE_NOT_SAME);
        param2.setError(PASSWORD_ARE_NOT_SAME);
        return false;
    }


    public static boolean isCorrectPassword(EditText param){
        if (param.getText().toString().trim().isEmpty()
                || param.getText().toString().length() < 3 ) {
            param.requestFocus();
            param.setError(PASSWORD_WRONG_LENGTH);
            return false;
        }
        return true;
    }

    public static boolean check(Context context,TextInputEditText... params){
        boolean correctFields = true;
        for (TextInputEditText param : params) {
            if(param.getText().toString().trim().isEmpty()) {
                param.requestFocus();
                param.setError(context.getString(R.string.incorrect_values));
                correctFields = false;
            }
        }
        return correctFields;
    }


    public   static boolean isValidEmail(EditText param) {
        if (param.getText().toString().trim().isEmpty()
                || param.getText().toString().length() < 3
                || TextUtils.isEmpty(param.getText().toString())
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(param.getText().toString()).matches()) {
            param.requestFocus();
            param.setError(PASSWORD_WRONG_LENGTH);
            return false;
        }
        return true;
    }
}
