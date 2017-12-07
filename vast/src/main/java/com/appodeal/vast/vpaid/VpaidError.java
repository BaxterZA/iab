package com.appodeal.vast.vpaid;


public class VpaidError extends Exception {
    VpaidError() {
        super();
    }

    VpaidError(String detailMessage) {
        super(detailMessage);
    }

    VpaidError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    VpaidError(Throwable throwable) {
        super(throwable);
    }
}
