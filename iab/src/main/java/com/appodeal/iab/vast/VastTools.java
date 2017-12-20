package com.appodeal.iab.vast;


import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.iab.Logger;

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

public class VastTools {
    private final static String TAG = "VastTools";
    
    private final static String SUPPORTED_STATIC_TYPE_REGEX = "image/.*(?i)(gif|jpeg|jpg|bmp|png)";
    static final int assetsColor = Color.parseColor("#B4FFFFFF");
    static final int backgroundColor = Color.parseColor("#52000000");
    static final String defaultCtaText = "Learn more";

    static final int defaultSkipTime = 5000;

    static String getStringFromNode(Node node) {
        String xml = null;
        Logger.d(TAG, "xmlNodeToString");
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
            Logger.e(TAG, e);
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
            Logger.e(TAG, e);
        }
        return document;
    }


    static String replaceMacros(@NonNull String url, @Nullable String mediaFileUrl, int playerPositionInMills, @Nullable com.appodeal.iab.vast.Error error) {
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
        if (error != null && error != com.appodeal.iab.vast.Error.ERROR_NONE && (url.contains("[ERRORCODE]") || url.contains("%5BERRORCODE%5D"))) {
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


    public static void removeFromParent(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
