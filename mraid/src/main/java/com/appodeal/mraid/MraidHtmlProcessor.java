package com.appodeal.mraid;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MraidHtmlProcessor {

    static String processRawHtml(String rawHtml, MraidEnvironment environment) {
        //Add MRAID_ENV to creative
        String ls = System.getProperty("line.separator");

        String stringBuilder = "<script>" + ls +
                "var MRAID_ENV = window.MRAID_ENV =" +
                environment.toMraidString() +
                ";" +
                ls +
                "</script>" +
                rawHtml;

        StringBuffer processedHtml = new StringBuffer(stringBuilder);

        String regex;
        Pattern pattern;
        Matcher matcher;

        // Add html, head, and/or body tags as needed.
        boolean hasHtmlTag = (rawHtml.contains("<html"));
        boolean hasHeadTag = (rawHtml.contains("<head"));
        boolean hasBodyTag = (rawHtml.contains("<body"));

        // basic sanity checks
        if ((!hasHtmlTag && (hasHeadTag || hasBodyTag)) || (hasHtmlTag && !hasBodyTag)) {
            return rawHtml;
        }

        if (!hasHtmlTag) {
            processedHtml.insert(0, "<html>" + ls + "<head>" + ls + "</head>" + ls + "<body><div>" + ls); //Was align='center'
            processedHtml.append("</div></body>").append(ls).append("</html>");
        } else if (!hasHeadTag) {
            // html tag exists, head tag doesn't, so add it
            regex = "<html[^>]*>";
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(processedHtml);
            int idx = 0;
            while (matcher.find(idx)) {
                processedHtml.insert(matcher.end(), ls + "<head>" + ls + "</head>");
                idx = matcher.end();
            }
        }

        // Add meta and style tags to head tag.
        String metaTag = "<meta name='viewport' content='width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no' />";

        String styleTag = "<style>" + ls +
                "body { margin:0; padding:0;}" + ls +
                "*:not(input) { -webkit-touch-callout:none; -webkit-user-select:none; -webkit-text-size-adjust:none; }" + ls +
                "</style>";

        regex = "<head[^>]*>";
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(processedHtml);
        int idx = 0;
        while (matcher.find(idx)) {
            processedHtml.insert(matcher.end(), ls + metaTag + ls + styleTag);
            idx = matcher.end();
        }

        return processedHtml.toString();
    }

}
