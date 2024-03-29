package com.appodeal.vast;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

class VastTools {
    private final static String SUPPORTED_STATIC_TYPE_REGEX = "image/.*(?i)(gif|jpeg|jpg|bmp|png)";
    static final int assetsColor = Color.parseColor("#B4FFFFFF");
    static final int shadowColor = Color.parseColor("#5c000000");
    static final int backgroundColor = Color.parseColor("#52000000");
    static final String defaultCtaText = "Learn more";
    static final String repeat = "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";
    static final String close = "iVBORw0KGgoAAAANSUhEUgAAAJEAAACRCAYAAADD2FojAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAKwwAACsMBNCkkqwAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAcdEVYdFNvZnR3YXJlAEFkb2JlIEZpcmV3b3JrcyBDUzbovLKMAAAE1UlEQVR4nO3dva4bRRiH8efwJSj3EqAApYjScBVUiASJ6+IqiKKgVFxFOopISBGXsCURCJnieI4TzrG99rsz+378n9KytVP8tNqdnfHe7HY7lLL00dYDUPETImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImRMiZU6IlDkhUuaESJkTImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImftk5MHmeX4MPAGeT9P0z8hjZ26e54+BZ8DbaZpejz7+sDPRHtD3wFfAT/M8fzrq2JnbA/oR+Ab4bp7nb0ePYQii9wDd7D/6EkEy9x6gr9/7eDik7ogeANQSJENHALWGQuqK6ASgliBd0RlArWGQuiFaAKglSBe0EFBrCKSeZ6JHnAfUEqQFXQio9ajTcO7qiegl8McF3xekE10J6E/glz4jOtQN0TRN/wIvECRzFkAj5uO6XlgLkj3vgGDALb4gXV8EQDBoslGQLi8KIBj42EOQlhcJEAx+ii9I54sGCDZYCiJIx4sICDZaTyRI94sKCDZclCZIhyIDgo1XNgpSfEDgYHlsZUgZAIEDRFATUhZA4AQR1IKUCRA4QgQ1IGUDBM4QQW5IGQGBQ0SQE1JWQOAUEeSClBkQOEYEOSBlBwTOEUFsSBUAQQBEEBNSFUAQBBHEglQJEARCBDEgVQMEwRCBb0gVAUFAROATUlVAEBQR+IJUGRAERgQ+IFUHBMERwbaQBOi28IhgG0gCdCgFIhgLSYA+LA0iGANJgO6XChH0hSRAD5cOEfSBJEDHS4kI1oUkQKdLiwjWgSRA57vZ7XZbj6F7BggvuP3zUgE6UQlEcDWkd8DnF3y/HCAohAiuhrS0koAg+TXR/7vyGmlJZQFBMUTQBVJpQFAQEawKqTwgKIoIVoEkQPvKIoI7SL8Cf13407+BlwJ0W2lE+7u1H4AvLvzpZ8DTrfe1eaksohVu911skPRQSUQrzhcJEgURdZhwLA+pFKKOM9alIZVBZHh2trSykEogMjzF/xkH+9q8lx6RcT3QO5xskPRcakRrLCjzsEHSe2kRrbkiUZBOlxJRjyWtgnS8dIh6rokWpIdLhWjEonpBul8aRCN3ZQjSh6VAtMW2HkE6FB7RlvvCBOm20Ig8bCwUpMCIPABqVYcUEpEnQK3KkMIh8gioVRVSKESeAbUqQgqDKAKgVjVIIRBFAtSqBMk9ooiAWlUguUYUGVCrAiS3iDIAamWH5BJRJkCtzJDcIcoIqJUVkitEmQG1MkJyg6gCoFY2SC4QVQLUygRpc0QVAbWyQNoUUWVArQyQNkMkQIeiQ9oEkQDdLzKk4YgE6HhRIQ1FJEDniwhpGCIBWl40SEMQCdDlRYLUHZEAXV8USF0RCZC9CJC6IRKg9fIOqeeZ6CkCtFoWSH1GdKgnojfA0jfyCdCCroT0ptNw7uqGaJqm34FXnIckQBd0IaTfpml63XlIfS+sF0ASoCtaCGkIIBhwi38CkgAZOgNpGCAYNNn4ACQBWqEjkIYCgsFvo57n+THwBHguQOu1n055BrwdDQiKvdJc9Wnz5bEqfkKkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEyJ0TKnBApc0KkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEy9x9PtEuyzhRHVgAAAABJRU5ErkJggg==";
    static final String unmute = "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";
    static final String mute = "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";

    static final int defaultSkipTime = 5000;

    static String getStringFromNode(Node node) {
        String xml = null;
        VastLog.d("xmlNodeToString");
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(sw));

            xml = sw.toString();
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return xml;
    }

    static String getElementValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        Node child;
        String value = "";
        CharacterData cd;

        for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
            child = childNodes.item(childIndex);
            if (!(child instanceof CharacterData)) {
                continue;
            }

            cd = (CharacterData) child;
            value = cd.getData().trim();

            if (value.length() == 0) {
                continue;
            }

            return value;
        }
        return value;
    }

    static Document getDocumentFromString(String doc) {
        DocumentBuilder db;
        Document document = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(doc));
            document = db.parse(is);
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }
        return document;
    }


    static String replaceMacros(@NonNull String url, @Nullable String mediaFileUrl, int playerPositionInMills, @Nullable Error error) {
        if (url.contains("[TIMESTAMP]") || url.contains("%5BTIMESTAMP%5D")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
            url = url.replace("[TIMESTAMP]", simpleDateFormat.format(new Date()));
            url = url.replace("%5BTIMESTAMP%5D", simpleDateFormat.format(new Date()));
        }
        if (url.contains("[CACHEBUSTING]") || url.contains("%5BCACHEBUSTING%5D")) {
            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                randomStringBuilder.append(String.valueOf(generator.nextInt(10)));
            }
            String randomString = randomStringBuilder.toString();
            url = url.replace("[CACHEBUSTING]", randomString);
            url = url.replace("%5BCACHEBUSTING%5D", randomString);
        }
        if (!TextUtils.isEmpty(mediaFileUrl) && (url.contains("[ASSETURI]") || url.contains("%5BASSETURI%5D"))) {
            url = url.replace("[ASSETURI]", mediaFileUrl);
            url = url.replace("%5BASSETURI%5D", mediaFileUrl);
        }
        if ((url.contains("[CONTENTPLAYHEAD]") || url.contains("%5BCONTENTPLAYHEAD%5D"))) {
            url = url.replace("[CONTENTPLAYHEAD]", getTimeStringFromMills(playerPositionInMills));
            url = url.replace("%5BCONTENTPLAYHEAD%5D", getTimeStringFromMills(playerPositionInMills));
        }
        if (error != null && error != Error.ERROR_NONE && (url.contains("[ERRORCODE]") || url.contains("%5BERRORCODE%5D"))) {
            url = url.replace("[ERRORCODE]", String.valueOf(error.getCode()));
            url = url.replace("%5BERRORCODE%5D", String.valueOf(error.getCode()));
        }
        return url;
    }


    static boolean isStaticResourceTypeSupported(String type) {
        return type.matches(SUPPORTED_STATIC_TYPE_REGEX);
    }

    static String getTimeStringFromMills(int timeInMills) {
        String hours = String.valueOf(timeInMills / (60 * 60 * 1000));
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String minutes = String.valueOf((timeInMills % (60 * 60 * 1000)) / (60 * 1000));
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        String seconds = String.valueOf((timeInMills % (60 * 1000)) / 1000);
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        String mills = String.valueOf(timeInMills % 1000);
        if (mills.length() < 3) {
            while (mills.length() < 3) {
                mills = "0" + mills;
            }
        }
        return hours + ":" + minutes + ":" + seconds + "." + mills;
    }

    static int getMillsFromTimeString(String time) {
        String timeWithoutMills;
        int mills = 0;
        if (time.contains(".")) {
            timeWithoutMills = time.split("\\.")[0];
            mills = Integer.valueOf(time.split("\\.")[1]);
        } else {
            timeWithoutMills = time;
        }
        String[] units = timeWithoutMills.split(":");
        if (units.length == 3) {
            int hours = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);
            int seconds = Integer.parseInt(units[2]);
            return (hours * 60 * 60 + minutes * 60 + seconds) * 1000 + mills;
        } else if (units.length == 2) {
            int minutes = Integer.parseInt(units[0]);
            int seconds = Integer.parseInt(units[1]);
            return (minutes * 60 + seconds) * 1000 + mills;
        }
        return 0;
    }

    static boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equals("1");
    }

    static void fireUrl(String url) {
        safeExecute(new TrackingTask(), url);
    }

    static <P> void safeExecute(AsyncTask<P, ?, ?> asyncTask, P... params) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    static float getScreenDensity(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    static float getDisplayAspectRatio(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (float) size.x / size.y;
    }

    static Bitmap getBitmapFromBase64(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    static void removeFromParent(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }



}
