package com.appodeal.iab.mraid;


public class MraidError extends Exception {
    MraidError() {
        super();
    }

    MraidError(String detailMessage) {
        super(detailMessage);
    }

    MraidError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    MraidError(Throwable throwable) {
        super(throwable);
    }
}
