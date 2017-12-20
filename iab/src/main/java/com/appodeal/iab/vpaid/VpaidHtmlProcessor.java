package com.appodeal.iab.vpaid;


class VpaidHtmlProcessor {

    static String processHtml(String url) {
        return "<html>\n" +
                "  <head>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no' />\n" +
                "    <style>\n" +
                "       body { margin: 0; padding: 0; background-color: black; position: fixed; } *:not(input) { -webkit-touch-callout: none; -webkit-user-select: none; -webkit-text-size-adjust: none; } #videoElement { position: fixed; top: 0; left: 0; width: 100% } \n" +
                "    </style>\n" +
                "    <script type='application/javascript' src='" + url + "'></script>\n" +
                "    <script type='application/javascript'>\n" +
                Assets.vpaidJs +
                "    </script>\n" +
                "    <title></title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id='videoAdLayer' style='height: 100%; width: 100%'>\n" +
                "      <video id='videoElement' poster='data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs='>Your browser does\n" +
                "      not support the video tag.</video>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
    }

}
