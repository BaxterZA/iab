package com.appodeal.iab.vast;


import android.support.annotation.Nullable;
import android.util.Log;

import com.appodeal.iab.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class VastProcessor {
    private final static String TAG = "VastProcessor";
    
    private static final String VAST_AD_TAG = "VASTAdTagURI";
    private static final int MAX_VAST_LEVELS = 5;
    private final StringBuilder mergedVastDocs = new StringBuilder();

    @Nullable
    VastModel loadUrl(String link)  {
        return loadXml(getXmlFromUrl(link));
    }

    @Nullable
    VastModel loadXml(String xml) {
        com.appodeal.iab.vast.Error error = processXml(xml, 0);
        VastModel vastModel = createModel();

        if (error != com.appodeal.iab.vast.Error.ERROR_NONE && vastModel != null) {
            vastModel.sendError(error);
        }

        return vastModel;
    }

    @Nullable
    private VastModel createModel() {
        try {
            Document mainDoc = wrapMergedVastDocumentsWithVasts();
            return new VastModel(mainDoc);
        } catch (Exception ignore) {
            Logger.d(TAG, "Can't create vast model");
        }
        return null;
    }

    private Document wrapMergedVastDocumentsWithVasts() {
        Logger.d(TAG, "Wrap merged vast documents with Vasts");
        mergedVastDocs.insert(0, "<VASTS>");
        mergedVastDocs.append("</VASTS>");

        String merged = mergedVastDocs.toString();
        Logger.v(TAG, "Merged VAST doc:\n" + merged);

        return VastTools.getDocumentFromString(merged);
    }

    private com.appodeal.iab.vast.Error processXml(String xml, int depth) {
        if (xml == null) {
            return com.appodeal.iab.vast.Error.ERROR_CODE_XML_PARSING;
        }

        Logger.d(TAG, String.format("Start process xml: %s", xml));

        if (depth >= MAX_VAST_LEVELS) {
            return com.appodeal.iab.vast.Error.ERROR_CODE_EXCEEDED_WRAPPER_LIMIT;
        }

        Document newDocument = createDoc(xml);
        if (newDocument == null) {
            return com.appodeal.iab.vast.Error.ERROR_CODE_XML_PARSING;
        }

        merge(newDocument);

        if (itsWrapper(newDocument)) {
            Logger.d(TAG, "Document is a wrapper");
            return processXml(getNextXml(newDocument), depth + 1);
        } else {
            return com.appodeal.iab.vast.Error.ERROR_NONE;
        }
    }

    private boolean itsWrapper(Document document) {
        NodeList adTagUriNode = document.getElementsByTagName(VAST_AD_TAG);
        return adTagUriNode != null && adTagUriNode.getLength() > 0;
    }

    @Nullable
    private String getNextXml(Document document) {
        NodeList adTagUriNode = document.getElementsByTagName(VAST_AD_TAG);
        Node node = adTagUriNode.item(0);
        String nextUri = VastTools.getElementValue(node);
        Logger.d(TAG, String.format("Wrapper URL: %s", nextUri));

        return getXmlFromUrl(nextUri);
    }

    @Nullable
    private Document createDoc(String xml) {
        Logger.d(TAG, "Creating document from string");
        try {
            Document doc = VastTools.getDocumentFromString(xml);
            doc.getDocumentElement().normalize();
            Logger.d(TAG, "Document successfully created.");
            return doc;
        } catch (Exception e) {
            Logger.e(TAG, e);
            return null;
        }
    }

    private void merge(Document newDoc) {
        Logger.d(TAG, "Merging new document into main document");
        Node newDocElement = newDoc.getElementsByTagName("VAST").item(0);
        String doc = VastTools.getStringFromNode(newDocElement);
        mergedVastDocs.append(doc);

        Logger.d(TAG, "Merge successful.");
    }

    @Nullable
    private String getXmlFromUrl(String link) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    return readURLConnectionResponse(urlConnection.getInputStream());
                default:
                    return null;
            }
        } catch (Exception e) {
            Log.e("VastProcessor", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private String readURLConnectionResponse(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder(inputStream.available());
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        } catch (Exception e) {
            Log.e("VastProcessor", e.getMessage());
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Log.e("VastProcessor", e.getMessage());
            }
        }
    }
}
