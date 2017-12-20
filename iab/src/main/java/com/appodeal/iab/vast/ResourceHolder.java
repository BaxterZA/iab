package com.appodeal.iab.vast;


interface ResourceHolder {

    HtmlResource getHtmlResource();

    StaticResource getStaticResource();

    IFrameResource getIFrameResource();

    String getClickThrough();

    boolean hasValidStaticResource();
}
