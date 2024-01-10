package com.example.whatsfood;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//To use this, copy 2 lines below to onCreate class activity
//FormatTextWatcher watcher1 = new FormatTextWatcher(your_editText);
//watcher1.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_ONLY_ALPHABET); -> Modify this

public class FormatTextWatcher implements TextWatcher {
    public enum mode {
        CHECK_EMPTY,
        CHECK_LENGTH,
        CHECK_HAS_SPECIAL_CHARACTER,
        CHECK_NO_SPECIAL_CHARACTER,
        CHECK_HAS_LOWERCASE,
        CHECK_HAS_UPPERCASE,
        CHECK_HAS_NUMBER,
        CHECK_IS_EMAIL,
        CHECK_ONLY_NUMBER,
        CHECK_ONLY_ALPHABET,
    }
    public int minLength;
    public int maxLength;
    private EditText editText;
    public Set<mode> modes;

    public FormatTextWatcher(EditText editText) {
        this.editText = editText;
        this.editText.addTextChangedListener(this);
    }
    public FormatTextWatcher(EditText editText, int minLength, int maxLength) {
        this.editText = editText;
        this.editText.addTextChangedListener(this);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = editText.getText().toString();
        if (modes.contains(mode.CHECK_EMPTY) && str.isEmpty()) {
            editText.setError("Field cannot be empty");
            return;
        }
        if (modes.contains(mode.CHECK_LENGTH) && (str.length() < minLength || str.length() > maxLength)) {
            editText.setError("Length of input must be in range[" + minLength + ", " + maxLength + "]");
            return;
        }
        if (modes.contains(mode.CHECK_ONLY_NUMBER) && !str.matches("[0-9]+")) {
            editText.setError("Input must contain only numbers");
            return;
        }
        if (modes.contains(mode.CHECK_ONLY_ALPHABET) && !str.matches("[a-z A-Z]+")) {
            editText.setError("Input must contain only alphabet letters");
            return;
        }
        if (modes.contains(mode.CHECK_NO_SPECIAL_CHARACTER) && has_special_character(str)) {
            editText.setError("Input cannot contain special characters");
            return;
        }
        if (modes.contains(mode.CHECK_HAS_SPECIAL_CHARACTER) && !has_special_character(str)) {
            editText.setError("Input must contain at least a special character");
            return;
        }
        if (modes.contains(mode.CHECK_HAS_LOWERCASE) && !has_lowercase(str)) {
            editText.setError("Input must contain at least a lowercase letter");
            return;
        }
        if (modes.contains(mode.CHECK_HAS_UPPERCASE) && !has_uppercase(str)) {
            editText.setError("Input must contain at least a uppercase letter");
            return;
        }
        if (modes.contains(mode.CHECK_HAS_NUMBER) && !has_number(str)) {
            editText.setError("Input must contain at least a number");
            return;
        }
        editText.setError(null);
    }

    public static boolean has_special_character(String str) {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(str);
        return hasSpecial.find();
    }

    public static boolean has_lowercase(String str) {
        Pattern lowercase = Pattern.compile("[a-z]");
        Matcher hasLowercase = lowercase.matcher(str);
        return hasLowercase.find();
    }

    public static boolean has_uppercase(String str) {
        Pattern uppercase = Pattern.compile("[A-Z]");
        Matcher hasUppercase = uppercase.matcher(str);
        return hasUppercase.find();
    }

    public static boolean has_number(String str) {
        Pattern digit = Pattern.compile("[0-9]");
        Matcher hasDigit = digit.matcher(str);
        return hasDigit.find();
    }
}
