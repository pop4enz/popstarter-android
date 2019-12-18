package com.pop4enz.popstarter.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {

    private static final String FULL_TIME_FORMAT ="EEE MMM dd HH:mm:ss";
    private static final String DEFAULT_IMAGE
            = "https://sun9-14.userapi.com/c854120/v854120120/179b7e/5MAug30XaaM.jpg";

    public static String getFormattedDate(Date date) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(FULL_TIME_FORMAT, Locale.ROOT);
            return utcFormat.format(date);
    }

    public static Boolean isNotEmpty(String string) {
        return (string != null && !string.isEmpty());
    }

    public static void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null && !imagePath.isEmpty()) {
            loadImageInto(imagePath, imageView);
        } else {
            loadImageInto(DEFAULT_IMAGE, imageView);
        }
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void loadImageInto(String uri, ImageView imageView) {
        Picasso.get().load(uri).into(imageView);
    }

}
