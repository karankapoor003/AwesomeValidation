package com.strohwitwer.awesomevalidation.validators;

import android.app.Activity;
import android.widget.EditText;

import com.strohwitwer.awesomevalidation.ValidationHolder;
import com.strohwitwer.awesomevalidation.utility.ValidationCallback;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validator {

    protected ArrayList<ValidationHolder> mValidationHolderList;

    public Validator() {
        mValidationHolderList = new ArrayList<ValidationHolder>();
    }

    public void set(EditText editText, String regex, String errMsg) {
        Pattern pattern = Pattern.compile(regex);
        ValidationHolder validationHolder = new ValidationHolder(editText, pattern, errMsg);
        mValidationHolderList.add(validationHolder);
    }

    public void set(Activity activity, int viewId, String regex, int errMsgId) {
        EditText editText = (EditText) activity.findViewById(viewId);
        String errMsg = activity.getResources().getString(errMsgId);
        set(editText, regex, errMsg);
    }

    protected boolean checkFields(ValidationCallback callback) {
        boolean result = true;
        boolean hasFailed = false;
        for (ValidationHolder validationHolder : mValidationHolderList) {
            Matcher matcher = validationHolder.getPattern().matcher(validationHolder.getText());
            if (!matcher.matches()) {
                callback.execute(validationHolder, matcher);
                if (!hasFailed) {
                    EditText editText = validationHolder.getEditText();
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    hasFailed = true;
                }
                result = false;
            }
        }
        return result;
    }

    public abstract boolean trigger();

    public abstract void halt();

}