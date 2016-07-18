package com.example.apc4.retrofitwithrepositrystructure.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.apc4.retrofitwithrepositrystructure.BuildConfig;
import com.example.apc4.retrofitwithrepositrystructure.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/*
* Common Utils of App
* */
public class Utils {
    public final static int REQUEST_CAMERA = 23;
    public final static int REQUEST_GALLERY = 22;


    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideToolbar(Context context) {

        View decorView = ((AppCompatActivity) context).getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }



    public static void infoLog(String tag, String message) {
        if (BuildConfig.DEBUG) {
            System.out.println(tag + " : " + message);
        }
    }

    public static void redLog(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag + " : ", message);
        }
    }

    public static void printUrl(String tag, String url) {
        if (BuildConfig.DEBUG) {
            Log.e("", "");
        }
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Spanned getFormattedString(){
        return Html.fromHtml("I read & agreed RYDR's " +
                "<b>Terms</b> and " +
                "<b>Privacy Policy</b>");
    }


    public static void startCameraByIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public static void startGalleryByIntent(Activity activity) {
        Intent intent1 = new Intent();
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("image/*");
        activity.startActivityForResult(intent1, REQUEST_GALLERY);
    }

    public static int getOrientation(Activity activity, Uri photoUri) {
        /* it's on the external media. */
        int value;

        Cursor cursor = activity.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        if (cursor.getCount() == 0) {
            return -1;
        }
        cursor.moveToFirst();
        value = cursor.getInt(0);
        cursor.close();
        return value;
    }

    public static void appToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RequestBody convertFileToRequestBody(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }

    public static RequestBody convertStringToRequestBody(String str) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), str);
    }

    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5 ExifInterface exif =
             * new ExifInterface(src); orientation =
             * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class
                        .forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass
                        .getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                        new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass
                        .getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance,
                        new Object[] { tagOrientation, 1 });
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float px = dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static String formatDecimalPlaces(String value) {
        return String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(value));
    }

    public static String formatDate(String dateString){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date startDate;
        String newDateString = "";
        try {
            startDate = df.parse(dateString);
            newDateString = outFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    public static String getZoneFormattedDate(String dateStr, String inFormat, String outFormat) {

        String time = dateStr.replace("T", " ");
        time = time.replace("Z", "");
        SimpleDateFormat inFormatter = new SimpleDateFormat(inFormat);
        SimpleDateFormat outFormatter = new SimpleDateFormat(outFormat);
        inFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = inFormatter.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        outFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outFormatter.format(date);

    }

    public static String getFormattedDate(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(System.currentTimeMillis()));
    }

    public static String getTimeDifference(String dateStr, String format) {
        String time = dateStr.replace("T", " ");
        time = time.replace("Z", "");

        Date fromDate;
        long fromMillies, currentMillies;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            fromDate = simpleDateFormat.parse(time);
            fromMillies = fromDate.getTime();
            currentMillies = System.currentTimeMillis();

            long hour = 1000 * 60 * 60;
            long day = 1000 * 60 * 60 * 24;
            long week = 1000 * 60 * 60 * 24 * 7;

            if (currentMillies - fromMillies < week) {
                if (currentMillies - fromMillies < day) {
                    if (currentMillies - fromMillies < hour) {
                        return ((currentMillies - fromMillies) / (1000 * 60)) + "min ago";
                    } else {
                        return ((currentMillies - fromMillies) / hour) + "hr ago";
                    }
                } else {
                    return new SimpleDateFormat("EEE").format(fromDate);
                }
            } else {
                return new SimpleDateFormat("MMM dd").format(fromDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getListFormattedDate(String dateStr, String format) {
        String time = dateStr.replace("T", " ");
        time = time.replace("Z", "");

        Date fromDate;
        long fromMillies, currentMillies;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            fromDate = simpleDateFormat.parse(time);
            fromMillies = fromDate.getTime();
            currentMillies = System.currentTimeMillis();

            long hour = 1000 * 60 * 60;
            long day = 1000 * 60 * 60 * 24;
            long week = 1000 * 60 * 60 * 24 * 7;


            if (currentMillies - fromMillies < week) {

                if (currentMillies - fromMillies < day) {

                    //Within day format "3:09 PM"
                    return new SimpleDateFormat("hh:mm a").format(fromDate);
                } else {

                    //Within week format "Thu 3:09 PM"
                    return new SimpleDateFormat("EEE hh:mm a").format(fromDate);
                }
            } else {
                //More than week format "6 Mar 3:09 PM"
                return new SimpleDateFormat("dd MMM hh:mm a").format(fromDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void hideKeyboard(Activity context){
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
