package com.appodeal.iab.vast;


class StaticResource {
    private final String creativeType;
    private final String uri;

    StaticResource(String creativeType, String uri) {
        this.creativeType = creativeType;
        this.uri = uri;
    }

    String getCreativeType() {
        return creativeType;
    }

    String getUri() {
        return uri;
    }

    String getHtml(String clickThrough) {
        String style = "<style type='text/css'>" +
                "@media screen and (orientation:portrait) {img {width:100%; height:auto;}}" +
                "@media screen and (orientation:landscape) {img {width:auto; height:100%;}}" +
                "body {margin:0px !important; padding:0px !important; zoom: 0 !important;}" +
                "div {height:100%; width:100%; display:block; position:relative;}" +
                "img {bottom:0; left:0; margin:auto; max-height:100%; max-width:100%; position:absolute; right:0; top:0;}" +
                "</style>";

        return String.format("%s<a href='%s'><img src='%s'/></a>", style, clickThrough, uri);
    }
}
